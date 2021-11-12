package com.example.smarttag.RetrofitRepositories;

import android.util.Log;

import com.example.smarttag.Api.KrotApi;
import com.example.smarttag.Models.DeviceInfo;
import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.Session;
import com.example.smarttag.Utils.HTTPCODES;
import com.example.smarttag.ViewModels.WelcomeScreen.WelcomeEvent;
import com.example.smarttag.ViewModels.WelcomeScreen.WelcomeEventsTypes;
import com.example.smarttag.ViewModels.WelcomeScreen.WelcomeViewModel;
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
   // private static final String  baseurl = "https://sfc.rniirs.ru/Api/";
    private static final String  baseurl = "http://192.168.0.100:8080/";

    private KrotApi krotApi;
    private Session openedSession;
    private static Retrofit retrofit;
    private static KrotRepository instance;


    private KrotRepository(){
       init();
    }

    public static KrotRepository getInstance(){
        if (instance == null) {
            instance = new KrotRepository();
        }
        return instance;
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
        krotApi = retrofit.create(KrotApi.class);
    }

    public void startSession(WelcomeViewModel viewModel){

        Call<Session> sessionCall = krotApi.handshake(new DeviceInfo());
        sessionCall.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if(response.isSuccessful()||response.code()== HTTPCODES.HTTP_CODES_OK) {
                    openedSession = response.body();
                    viewModel.onRequestPerformed(
                            new WelcomeEvent(WelcomeEventsTypes.SESSION_EVENT,response.body()));
                }  else {
                    viewModel.onRequestPerformed(null);
                }
            }
            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                Log.d("dogs",t.getMessage());
                viewModel.onRequestPerformed(null);
            }
        });

    }

    public void registerUserInfo(UserInfo userInfo, WelcomeViewModel viewModel){
        if(openedSession!=null) {
            Call<Void> registrationCall = krotApi.registration(
                    openedSession.getApikey(),
                    openedSession.getClient_id(),
                    userInfo
            );
            registrationCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if(response.isSuccessful()||response.code()==HTTPCODES.HTTP_CODES_OK){
                        viewModel.onRequestPerformed(new WelcomeEvent(WelcomeEventsTypes.REGISTRATION_EVENT,true));
                    } else{
                        viewModel.onRequestPerformed(new WelcomeEvent(WelcomeEventsTypes.REGISTRATION_EVENT,false));
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.d("dogs",t.getMessage());
                    viewModel.onRequestPerformed(new WelcomeEvent(WelcomeEventsTypes.REGISTRATION_EVENT,false));
                }
            });
        }
    }
}
