package com.example.smarttag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
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
import com.example.smarttag.Models.GpsEvent;
import com.example.smarttag.Services.BluetoothService;
import com.example.smarttag.Services.GpsService;
import com.example.smarttag.ViewModels.Presentation.ForegroundEvent;
import com.example.smarttag.ViewModels.Presentation.PresentationViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;
import com.example.smarttag.Views.BluetoothFragment;
import com.example.smarttag.Views.ChatFragment;
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
                if(bleEvt!=null){
                    Location location = gpsService.getActualLocation();
                    if(gpsService.validateLocation(location)) {
                        onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(PresentationActivity.this, R.drawable.bluetooth), "Smart Tag event",
                                bleEvt.getBleDev().getBleDev_Name() + " has sent " + bleEvt.getBleEvt_NumMsg() + "msg\n" + " Sending it with actual date"));
                        bleEvt.setBleEvt_Lat(location.getLatitude());
                        bleEvt.setBleEvt_Long(location.getLongitude());
                        bleEvt.setBleEvt_Alt(location.getAltitude());
                    } else {
                        onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(PresentationActivity.this, R.drawable.bluetooth), "Smart Tag event",
                                bleEvt.getBleDev().getBleDev_Name() + " has sent " + bleEvt.getBleEvt_NumMsg() + "msg\n" + " Will be sent with 0.0.0 coordinates due to expired date"));
                    }

                    viewmodel.sendNewBleEvent(bleEvt);
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
        registerReceiver(eventReciever,new IntentFilter("ACTION_SMART_TAG"));
        viewmodel = new ViewModelProvider(this).get(PresentationViewModel.class);
        viewmodel.getProcessingEvents().observe(this, new Observer<ViewModelEvent>() {
            @Override
            public void onChanged(ViewModelEvent viewModelEvent) {
                switch (viewModelEvent.getWe_type()){
                    case PresentationViewModel.PresentationEventsTypes
                            .GPS_EVENT : {
                        Boolean eventResult = (Boolean) viewModelEvent.getObject();
                        if(eventResult!=null)
                            onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(PresentationActivity.this,R.drawable.location),
                                    "Gps event transfer",eventResult.toString()));
                        else {
                            onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(PresentationActivity.this,R.drawable.location),
                                    "Gps event transfer","Poor connection detected. Delivery not warranted "));
                        }
                        break;
                    }
                    case PresentationViewModel.PresentationEventsTypes.BLUETOOTH_EVENT: {
                        Integer bluetoothResult = (Integer) viewModelEvent.getObject();
                        if(bluetoothResult == PresentationViewModel.PresentationEventsTypes.BLUETOOTH_EVENT_PLUS){
                            BluetoothFragment bluetoothFragment = (BluetoothFragment) getSupportFragmentManager().findFragmentByTag("ble_fragment");
                            if(bluetoothFragment!=null) {
                                bluetoothFragment.onNewDeviceAllowed();
                            }
                        }
                        break;
                    }
                }
            }
        });



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
                                    viewmodel.SendNewGpsEvent(new GpsEvent(location.getTime(),location.getLatitude(),location.getLongitude(),location.getAltitude()));
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
                switch (item.getItemId()){
                    case R.id.Menu_Bluetooth: {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.Presentation_ViewHolder,new BluetoothFragment(),"ble_fragment")
                                .commit();
                        break;
                    }
                    case R.id.Menu_Chat:  {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.Presentation_ViewHolder,new ChatFragment(),"chat_fragment")
                                .commit();
                        break;
                    }
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
        if(this.bluetoothService!=null)
            return this.bluetoothService.isAlive();
        else return false;
    }
    public Boolean getGpsServiceStatus(){
        if(this.gpsService!=null)
            return this.gpsService.isAlive();
        else return false;
    }
    public Boolean getScanStatus() {
        if(this.bluetoothService!=null)
            return this.bluetoothService.getScanMode();
        else return false;
    }
    public void toogleScanMode(Boolean status){
       this.bluetoothService.setScanMode(status);
    }

    public Boolean getRequestStatus() {
        if(bluetoothService!=null)
            return this.bluetoothService.isInRequest();
        else return false;
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