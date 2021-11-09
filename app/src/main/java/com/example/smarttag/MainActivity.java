package com.example.smarttag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.smarttag.ViewModels.WelcomeViewModel;
import com.example.smarttag.Views.Components.StatusTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.Welcome_Status)
    StatusTextView loadingStatus;
    WelcomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);


    }



    @Override
    protected void onResume() {
        loadingStatus.appendNeutral("Проверяю разрешения");
        if(preparePermissions()){
            loadingStatus.appendNeutral("Собираю данные о клиенте");
            loadingStatus.appendNeutral("Устанавливаю соединение с сервером");

            viewModel.startSesion();
        }
        viewModel.getSessionLiveData().observe(this, new Observer<Session>() {
            @Override
            public void onChanged(Session session) {
                if(session.apikey==null){
                    loadingStatus.appendError("Ошибка соединения");
                }
                else {
                    loadingStatus.appendSuccess("Соединение установлено");
                    if(session.is_new_client){
                        loadingStatus.appendNeutral("Обнаружен новый клиент");
                    }
                }
            }
        });
        super.onResume();
    }

    private boolean preparePermissions(){
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
            loadingStatus.appendSuccess("Разрешения получены");
            return true;
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
                       loadingStatus.appendError("Ошибка получения разрешений!");
                   } else
                        preparePermissions();
               }
            }
        }
    }
}