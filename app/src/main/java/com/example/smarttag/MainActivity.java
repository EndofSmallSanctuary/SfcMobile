package com.example.smarttag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.Services.BluetoothService;
import com.example.smarttag.Services.GpsService;
import com.example.smarttag.ViewModels.ViewModelEvent;
import com.example.smarttag.ViewModels.WelcomeScreen.WelcomeEventsTypes;
import com.example.smarttag.ViewModels.WelcomeScreen.WelcomeViewModel;
import com.example.smarttag.Views.Components.StatusTextView;
import com.example.smarttag.Views.RegistationFragment;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.Welcome_Status)
    StatusTextView loadingStatus;
    WelcomeViewModel viewModel;
    Boolean isRegistrationPassed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);

        loadingStatus.appendNeutral(getString(R.string.welcome_permissionscheck));


         if(preparePermissions()){
            onPermissionsGranted();
        }
        viewModel.getSessionLiveData().observe(this, new Observer<ViewModelEvent>() {
            @Override
            public void onChanged(ViewModelEvent viewModelEvent) {
                switch (viewModelEvent.getWe_type()) {
                    case WelcomeEventsTypes
                            .SESSION_EVENT: {
                        Session session = (Session) viewModelEvent.getObject();
                        if (session.apikey == null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingStatus.appendError(getString(R.string.welcome_connectionerror));

                                }
                            });
                        } else {
                            loadingStatus.appendSuccess(getString(R.string.welcome_connectionestablished));
                            if (session.is_new_client) {
                                loadingStatus.appendNeutral(getString(R.string.welcome_newclient));
                                attachRegistationFragment();
                            } else {
                                isRegistrationPassed = true;
                                startActivity(new Intent(MainActivity.this,PresentationActivity.class));
                                finish();
                            }
                        }
                        break;
                    }
                    case WelcomeEventsTypes
                            .REGISTRATION_EVENT:{
                        Boolean registationStatus = (Boolean) viewModelEvent.getObject();
                        if(registationStatus!=null&&registationStatus){
                            Toasty.success(MainActivity.this,getResources().getString(R.string.welcome_registrationsuccess), Toasty.LENGTH_SHORT).show();
                            isRegistrationPassed = true;
                            startActivity(new Intent(MainActivity.this,PresentationActivity.class));
                            finish();
                        } else {
                            Toasty.error(MainActivity.this,getResources().getString(R.string.welcome_registrationfail), Toasty.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    default: {break;}
                }
            }
        });
    }

    private void onPermissionsGranted() {
        loadingStatus.appendNeutral(getString(R.string.welcome_retrievingdevinfo));
        loadingStatus.appendNeutral(getString(R.string.launching_services));
        startService(new Intent(this, GpsService.class));
        startService(new Intent(this, BluetoothService.class));
        loadingStatus.appendNeutral(getString(R.string.welcome_connectionestablish));
        viewModel.startSesion();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void registratesomebruh(UserInfo userInfo){
        viewModel.registrateSomebruh(userInfo);
    }

    private void attachRegistationFragment() {
        loadingStatus.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Welcome_Fragment_Container,new RegistationFragment())
                .commit();

    }



    private boolean preparePermissions(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                    checkSelfPermission(Manifest.permission.BLUETOOTH)+
                    checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN)+
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)+
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)+
                    checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)+
                    checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE)        != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION+
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1);
            } else {
                loadingStatus.appendSuccess(getString(R.string.weclome_permissionsgranted));
                return true;
            }
        } else {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                    checkSelfPermission(Manifest.permission.BLUETOOTH)+
                    checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN)+
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)+
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1);
            } else {
                loadingStatus.appendSuccess(getString(R.string.weclome_permissionsgranted));
                return true;
            }
        }


        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            for(int i=0;i<grantResults.length;i++){
               if(grantResults[i]!=0){
                   if(!shouldShowRequestPermissionRationale(permissions[i])){
                       loadingStatus.appendError(getString(R.string.welcome_permissionerror));
                   } else
                        preparePermissions();
                        return;
               }
            }
            onPermissionsGranted();
        }
    }


    @Override
    protected void onDestroy() {
        if(!isRegistrationPassed){
            stopService(new Intent(MainActivity.this,PresentationActivity.class));
            stopService(new Intent(MainActivity.this,PresentationActivity.class));
        }
        Log.d("status","on destroy");
        super.onDestroy();
    }
}