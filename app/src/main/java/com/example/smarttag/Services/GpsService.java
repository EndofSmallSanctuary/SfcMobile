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
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.smarttag.PresentationActivity;
import com.example.smarttag.R;

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
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d("status","location changed");
            List<String> locationProviders = locationManager.getProviders(true);
            Location offeredLocation = null;
            for (String provider : locationProviders){
                @SuppressLint("MissingPermission") Location l  = locationManager.getLastKnownLocation(provider);
                if(l==null){
                    continue;
                }
                if(validateLocation(l)) {
                    if (offeredLocation == null || l.getAccuracy() < offeredLocation.getAccuracy()) {
                        offeredLocation = l;
                    }
                }
                Log.d("status",provider+" offers a location at "+l.getTime());
            }

            if (offeredLocation!=null) {
                Log.d("status","current actual location will be at "+offeredLocation.getTime());
                actualLocation = offeredLocation;
            };
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

        Intent serviceNotificationIntent = new Intent(this, PresentationActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, serviceNotificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.gps_service_is_live))
                .setContentText("Gps service requiring ")
                .setSmallIcon(R.drawable.location)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        initiateProcessing();
        isAlive = true;
        return START_NOT_STICKY;
    }

    private void initiateProcessing() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toasty.error(getApplicationContext(),"Error during start Gps Service",Toasty.LENGTH_SHORT).show();
                return;
            }
            if(locationManager!=null){
                long UPDATE_INTERVAL = 1000;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                UPDATE_INTERVAL,0,locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        UPDATE_INTERVAL,0,locationListener);
               // actualLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
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
