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

import com.example.smarttag.Models.BleEvt;
import com.example.smarttag.R;

import java.util.Date;

import es.dmoral.toasty.Toasty;

public class BluetoothService extends Service {
    private final String CHANNEL_ID = "BlUETOOTH_SERVICE";
    Boolean isBinded = false;
    Boolean isInRequest = false;
    Boolean isAlive = false;
    Boolean scan_mode = false;
    BluetoothLeScanner scanner;
    private final IBinder binder = new BluetoothBinder();

    final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice bluetoothDevice = result.getDevice();
            if (bluetoothDevice != null) {
                if (bluetoothDevice.getName() != null && bluetoothDevice.getName().toLowerCase().contains("smart_tag")) {
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
                        BleEvt bleEvt = new BleEvt();
                        bleEvt.setBleEvt_RSSI((long) result.getRssi());
                        bleEvt.setBleEvt_NumMsg((long) readyMsg);
                        bleEvt.setBleEvt_Time(new Date().getTime());
                        intent.putExtra("dev_mac", device.getAddress());
                        intent.putExtra("dev_name", device.getName());
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
                Toasty.error(getApplicationContext(),"Bluetooth is not recognized", Toasty.LENGTH_SHORT).show();
            } else if (!bluetoothAdapter.isEnabled()){
                Toasty.error(getApplicationContext(),"Bluetooth is not enabled", Toasty.LENGTH_SHORT).show();
            } else {
                isInRequest = true;
                scanner = bluetoothAdapter.getBluetoothLeScanner();
                scanner.startScan(scanCallback);
            }
        } catch (Exception e ){
            isInRequest = false;
            Log.d("dogs",e.getMessage());
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        openNotificationChannel();
        isAlive = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isAlive = false;
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
