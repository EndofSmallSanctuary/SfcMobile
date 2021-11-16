package com.example.smarttag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;

import com.example.smarttag.Services.BluetoothService;
import com.example.smarttag.Services.GpsService;
import com.example.smarttag.Views.BluetoothFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class PresentationActivity extends AppCompatActivity {

    @BindView(R.id.sfc_navigation_bar)
    BottomNavigationView navigationView;
    BluetoothService bluetoothService;
    GpsService gpsService;


    private final ServiceConnection gpsServiceConnection =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GpsService.GpsBinder binder = (GpsService.GpsBinder) service;
            gpsService = binder.getService();
            BluetoothFragment bluetoothFragment = (BluetoothFragment) getSupportFragmentManager().findFragmentByTag("ble_fragment");
            if(bluetoothFragment!=null){
                bluetoothFragment.updateGpsServiceStatus();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private final ServiceConnection bluetoothServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothService.BluetoothBinder binder = (BluetoothService.BluetoothBinder)  service;
            bluetoothService = binder.getService();
            BluetoothFragment bluetoothFragment = (BluetoothFragment) getSupportFragmentManager().findFragmentByTag("ble_fragment");
            if(bluetoothFragment!=null){
                bluetoothFragment.updateBleServiceStatus();
                bluetoothService.startProcessing();
                bluetoothFragment.updateRequest();
                bluetoothFragment.updateScanMode();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        ButterKnife.bind(this);

        //Binding services
        bindService(new Intent(this,GpsService.class),gpsServiceConnection, Context.BIND_AUTO_CREATE);
        bindService(new Intent(this,BluetoothService.class),bluetoothServiceConnection,Context.BIND_AUTO_CREATE);

        //Default fragment - bluetooth ( mAin function)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Presentation_ViewHolder,new BluetoothFragment(),"ble_fragment")
                .commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("Item",item.getItemId()+"");
                switch (item.getItemId()){

                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(bluetoothServiceConnection);
        unbindService(gpsServiceConnection);
        stopService(new Intent(this, BluetoothService.class));
        stopService(new Intent(this,GpsService.class));
        super.onDestroy();
    }

    public Boolean getBleServiceStatus(){
        return this.bluetoothService.isAlive();
    }
    public Boolean getGpsServiceStatus(){
        return this.gpsService.isAlive();
    }
    public void toogleScanMode(Boolean status){
       this.bluetoothService.setScanMode(status);
    }
    public Boolean getScanStatus() {
        return this.bluetoothService.getScanMode();
    }
    public Boolean getRequestStatus() {
        return this.bluetoothService.isInRequest();
    }
    public void toogleRequest(boolean b) {
        if(b) {
            this.bluetoothService.startProcessing();
        } else {
            this.bluetoothService.stopProcessing();
        }
    }
}