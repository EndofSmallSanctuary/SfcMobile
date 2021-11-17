package com.example.smarttag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;

import com.example.smarttag.Models.BleEvt;
import com.example.smarttag.Services.BluetoothService;
import com.example.smarttag.Services.GpsService;
import com.example.smarttag.ViewModels.Presentation.ForegroundEvent;
import com.example.smarttag.ViewModels.Presentation.PresentationViewModel;
import com.example.smarttag.Views.BluetoothFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PresentationActivity extends AppCompatActivity {



    @BindView(R.id.sfc_navigation_bar)
    BottomNavigationView navigationView;
    BluetoothService bluetoothService;
    GpsService gpsService;
    BroadcastReceiver eventReciever = new EventReciever();
    PresentationViewModel viewmodel;
    Timer gpsTimer = new Timer();

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

    public class EventReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                BleEvt bleEvt = intent.getParcelableExtra("payload");
                String devName = intent.getStringExtra("dev_name");
                if(bleEvt!=null){
                    onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(PresentationActivity.this,R.drawable.bluetooth),"Smart Tag event",
                            devName+" has sent "+bleEvt.getBleEvt_NumMsg() + "msg"));
                }
            } catch (Exception e){
                Log.d("status",e.getMessage());
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        ButterKnife.bind(this);
        viewmodel = new ViewModelProvider(this).get(PresentationViewModel.class);
        registerReceiver(eventReciever,new IntentFilter("ACTION_SMART_TAG"));

        gpsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(getGpsServiceStatus()){
                    Location location = getActualLocation();
                    if(location!=null){
                        if(gpsService.validateLocation(location)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_GPS,
                                            getString(R.string.gps_location_arrived), "Data packet came at: " + ForegroundEvent.milisToStrDate(location.getTime()));
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_GPS,
                                          getString(R.string.location_expired), "Last packet came at: " + ForegroundEvent.milisToStrDate(location.getTime()));
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_GPS,
                                        getString(R.string.gps_location_is_null),getString(R.string.not_sending));
                            }
                        });

                    }
                }
            }
        },5000,5000);


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
        gpsTimer.cancel();
        unregisterReceiver(eventReciever);
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
    public Location getActualLocation(){
        return this.gpsService.getActualLocation();
    }
    public ArrayList<ForegroundEvent> getForegroundEvents() {return  this.viewmodel.getForegroundEvents(); }
    public void prepareSignatureRequest(int eventType,String event,String desc){
        switch (eventType){
            case ForegroundEvent.FOREGROUND_EVENT_TYPE_NETWORK: {
                onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(this,R.drawable.network),event,desc));
                break;
            }
            case ForegroundEvent.FOREGROUND_EVENT_TYPE_SCAN:{
                onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(this,R.drawable.bluetooth),event,desc));
                break;
            }
            case ForegroundEvent.FOREGROUND_EVENT_TYPE_GPS:{
                onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(this,R.drawable.location),event,desc));
            }
        }
    }

    public void onNewForegroundEvent(ForegroundEvent event){
        int index = viewmodel.addNewForegroundEvent(event);
        BluetoothFragment bluetoothFragment = (BluetoothFragment) getSupportFragmentManager().findFragmentByTag("ble_fragment");
        if(bluetoothFragment!=null) {
            bluetoothFragment.onNewForegroundEvent();
        }

    }





}