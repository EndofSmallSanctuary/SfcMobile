package com.example.smarttag.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.smarttag.Models.BleDev;
import com.example.smarttag.Models.BleEvt;
import com.example.smarttag.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class BluetoothService extends Service {

    private ArrayList<String> previouslyFoundDevs = new ArrayList<>(Arrays.asList("EC:14:4E:DE:1A:C9","FF:F1:7E:69:83:B6","D3:B8:0F:D1:43:DC",
            "DE:FE:CF:86:AC:D5"));


    private final String CHANNEL_ID = "BlUETOOTH_SERVICE";
    Boolean isBinded = false;
    Boolean isInRequest = false;
    Boolean isAlive = false;
    Timer timer = new Timer();
    Boolean scan_mode = false;
    BluetoothLeScanner scanner;
    private final IBinder binder = new BluetoothBinder();

    final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice bluetoothDevice = result.getDevice();
            if (bluetoothDevice != null) {
                if (bluetoothDevice.getName() != null && (bluetoothDevice.getName().toLowerCase().contains("smart_tag")) ||
                    bluetoothDevice.getAddress()!=null && previouslyFoundDevs.contains(bluetoothDevice.getAddress())) {
                    BluetoothDevice device = result.getDevice();
                    if (device.getName() != null && device.getName().toLowerCase().contains("smart")) {
                        byte[] bytes = result.getScanRecord().getBytes();
                        byte[] msg = new byte[4];
                        msg[0] = bytes[8];
                        msg[1] = bytes[9];
                        msg[2] = bytes[10];
                        msg[3] = bytes[11];
                        int readyMsg = byteArrayToInt(msg);


                        Intent intent = new Intent("ACTION_SMART_TAG");
                        BleEvt bleEvt = new BleEvt((long)result.getRssi(),new Date(),0.0d,0.0d,0.0d,(long)readyMsg,scan_mode);
                        BleDev bleDev = new BleDev();
                        bleDev.setBleDev_MAC(result.getDevice().getAddress());
                        bleDev.setBleDev_Name(result.getDevice().getName());
                        bleEvt.setBleDev(bleDev);
                        intent.putExtra("payload", bleEvt);
                        sendBroadcast(intent);
                    }
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };



    public Boolean isInRequest(){
        return isInRequest;
    }

    public void setScanMode(Boolean status) {
        scan_mode = status;
    }

    public Boolean getScanMode(){
        return this.scan_mode;
    }

    public Boolean isAlive() {
        return isAlive;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        isBinded = true;
        return this.binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBinded = false;
        return super.onUnbind(intent);
    }

    public void startProcessing(){
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null){
                Toasty.error(getApplicationContext(), getString(R.string.bluetooth_not_recognized), Toasty.LENGTH_SHORT).show();
            } else if (!bluetoothAdapter.isEnabled()){
                Toasty.error(getApplicationContext(), getString(R.string.bluetooth_not_enabled), Toasty.LENGTH_SHORT).show();
            } else {
                isInRequest = true;
                scanner = bluetoothAdapter.getBluetoothLeScanner();
                scanner.startScan(scanCallback);
                Toasty.success(getApplicationContext(), getString(R.string.bluetooth_enabled_successfully), Toasty.LENGTH_SHORT).show();

            }
        } catch (Exception e ){
            isInRequest = false;
        }
    }

    public void stopProcessing(){
        scanner.stopScan(scanCallback);
        scanner.flushPendingScanResults(scanCallback);
        isInRequest = false;
    }

    public int byteArrayToInt(byte[] b)
    {
        return (b[3] & 0xFF) + ((b[2] & 0xFF) << 8) + ((b[1] & 0xFF) << 16) + ((b[0] & 0xFF) << 24);
    }


    public class BluetoothBinder extends Binder{
        public BluetoothService getService() { return BluetoothService.this; }

    }


    /*TODO Why not to add notifications later :?*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        openNotificationChannel();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent("ACTION_SMART_TAG");
//                BleEvt bleEvt = new BleEvt((long)121,new Date(),0.0d,0.0d,0.0d,(long)117,scan_mode);
//                BleDev bleDev = new BleDev();
//                bleDev.setBleDev_MAC("DE:F3:CE:85:CD:15");
//                bleDev.setBleDev_Name("H212000 ");
//                bleEvt.setBleDev(bleDev);
//                intent.putExtra("payload", bleEvt);
//                sendBroadcast(intent);
//            }
//        },6000,6000);
        isAlive = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isAlive = false;
        timer.cancel();
        super.onDestroy();
    }


    private void openNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel innerServiceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Bluetooth Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(innerServiceChannel);
        }
    }

}
