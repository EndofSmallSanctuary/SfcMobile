package com.example.smarttag.RetrofitRepositories;

import android.util.Log;

import com.example.smarttag.Api.KrotApi;
import com.example.smarttag.Models.DeviceInfo;
import com.example.smarttag.Session;
import com.example.smarttag.ViewModels.WelcomeViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KrotRepository {
    private static final String  baseurl = "http://192.168.0.100:8080";
    private static Retrofit retrofit;
    private static KrotRepository instance;

    private KrotRepository(){
       init();
    }


    public static KrotRepository getInstance(){
        if(instance!=null){
            return instance;
        } else {
           instance = new KrotRepository();
           return instance;
        }
    }

    private void init(){
        if(retrofit ==null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(14, TimeUnit.SECONDS);
            httpClient.writeTimeout(14, TimeUnit.SECONDS);
            httpClient.readTimeout(14, TimeUnit.SECONDS);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
    }

    public void startSession(WelcomeViewModel viewModel){

        //Начинаю сессию

        KrotApi api = retrofit.create(KrotApi.class);
        Call<Session> sessionCall = api.handshake(new DeviceInfo());
        sessionCall.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if(response.isSuccessful()){
                    viewModel.onSessionRequestPerformed(response.body());
                }  else {
                    viewModel.onSessionRequestPerformed(null);
                }
            }
            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                viewModel.onSessionRequestPerformed(null);
            }
        });

    }
}
