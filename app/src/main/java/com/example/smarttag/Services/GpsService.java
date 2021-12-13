package com.example.smarttag.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.smarttag.PresentationActivity;
import com.example.smarttag.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class GpsService extends Service {
    private final Integer LOCATION_LIFE_TIME = 10000;

    Location actualLocation;
    LocationManager locationManager;
    private final String CHANNEL_ID = "GPS_CHANNEL";
    private final IBinder binder = new GpsBinder();
    Boolean isBinded = false;
    Boolean isAlive = false;
    PowerManager.WakeLock wakeLock;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            actualLocation = locationResult.getLastLocation();
        }
    };

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            //
        }
    };


    public Location getActualLocation(){
//        if(validateLocation(this.actualLocation))
//            return this.actualLocation;
//        else return null;
        return this.actualLocation;
    };

    public boolean validateLocation(Location location){
        return location != null && new Date().getTime() - location.getTime() <= LOCATION_LIFE_TIME;
    }

    public Boolean isAlive(){
        return this.isAlive;
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        isBinded = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBinded = false;
        return super.onUnbind(intent);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
        wakeLock.acquire();
        openNotificationChannel();

//        Intent serviceNotificationIntent = new Intent(this, PresentationActivity.class);
//        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, serviceNotificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.gps_service_is_live))
                .setContentText(getString(R.string.gps_service_description))
                .setSmallIcon(R.drawable.location)
                .build();

        startForeground(1,notification);

        initiateProcessing();
        isAlive = true;
        return START_NOT_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void initiateProcessing() {
        try {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(3000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());

          } catch (Exception e){
            Log.d("exp",e.getMessage());
        }
    }




    public class GpsBinder extends Binder {
        public GpsService getService() {return GpsService.this;}
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
        isAlive = false;
        wakeLock.release();
        super.onDestroy();
    }

    private void openNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel innerServiceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Gps Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(innerServiceChannel);
        }
    }
}
