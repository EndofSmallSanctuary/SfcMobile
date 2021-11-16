package com.example.smarttag.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class GpsService extends Service {
    Location actualLocation;
    LocationManager locationManager;
    private final IBinder binder = new GpsBinder();
    Boolean isBinded = false;
    Boolean isAlive = false;
    PowerManager.WakeLock wakeLock;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            List<String> locationProviders = locationManager.getProviders(true);
            Location offeredLocation = null;
            for (String provider : locationProviders){
                @SuppressLint("MissingPermission") Location l  = locationManager.getLastKnownLocation(provider);
                if(l==null){
                    continue;
                }
                if(offeredLocation == null || l.getAccuracy() < offeredLocation.getAccuracy()){
                    offeredLocation = l;
                }
            }

            if (offeredLocation!=null) {
                actualLocation = offeredLocation;
            };
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
                //
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            //
        }
    };


    public Location getActualLocation(){
        if(new Date().getTime() - this.actualLocation.getTime()<=10000)
            return this.actualLocation;
        else return null;
    };


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
        initiateProcessing();
        isAlive = true;
        return super.onStartCommand(intent, flags, startId);
    }

    private void initiateProcessing() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toasty.error(getApplicationContext(),"Error during start Gps Service",Toasty.LENGTH_SHORT).show();
                return;
            }
            if(locationManager!=null){
                long UPDATE_INTERVAL = 0;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        UPDATE_INTERVAL,0,locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        UPDATE_INTERVAL,0,locationListener);
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
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
        wakeLock.acquire();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        isAlive = false;
        wakeLock.release();
        super.onDestroy();
    }
}
