package com.example.smarttag.RetrofitRepositories;

import android.util.Log;

import com.example.smarttag.Api.KrotApi;
import com.example.smarttag.Models.BleDev;
import com.example.smarttag.Models.BleEvt;
import com.example.smarttag.Models.DeviceInfo;
import com.example.smarttag.Models.GpsEvent;
import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.Session;
import com.example.smarttag.Utils.HTTPCODES;
import com.example.smarttag.ViewModels.BluetoothFragment.BluetoothEventsTypes;
import com.example.smarttag.ViewModels.BluetoothFragment.BluetoothViewModel;
import com.example.smarttag.ViewModels.Presentation.PresentationEventsTypes;
import com.example.smarttag.ViewModels.Presentation.PresentationViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;
import com.example.smarttag.ViewModels.WelcomeScreen.WelcomeEventsTypes;
import com.example.smarttag.ViewModels.WelcomeScreen.WelcomeViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KrotRepository {
   // protected String  baseurl = "https://sfc.rniirs.ru/Api/";

   private static final String  baseurl = "http://192.168.0.100:8080/";

    protected KrotApi krotApi;
    protected Session openedSession;
    protected Retrofit retrofit;
    private static KrotRepository instance;


    protected KrotRepository(){
       init();
    }

    public static KrotRepository getInstance(){
        if (instance == null) {
            instance = new KrotRepository();
        }
        return instance;
    }

    private void init(){
        if(retrofit == null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(5, TimeUnit.SECONDS);
            httpClient.writeTimeout(5, TimeUnit.SECONDS);
            httpClient.readTimeout(5, TimeUnit.SECONDS);

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
                            new ViewModelEvent(WelcomeEventsTypes.SESSION_EVENT,response.body()));
                }  else {
                    viewModel.onRequestPerformed(null);
                }
            }
            @Override
            public void onFailure(Call<Session> call, Throwable t) {
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
                        viewModel.onRequestPerformed(new ViewModelEvent(WelcomeEventsTypes.REGISTRATION_EVENT,true));
                    } else{
                        viewModel.onRequestPerformed(new ViewModelEvent(WelcomeEventsTypes.REGISTRATION_EVENT,false));
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    viewModel.onRequestPerformed(new ViewModelEvent(WelcomeEventsTypes.REGISTRATION_EVENT,false));
                }
            });
        }
    }

    public void getAvailableBleDevs(BluetoothViewModel bluetoothViewModel){
        if(openedSession!=null){
            Call<ArrayList<BleDev>> bledevscall = krotApi.availablebledevs(openedSession.getApikey(),openedSession.getClient_id());
            bledevscall.enqueue(new Callback<ArrayList<BleDev>>() {
                @Override
                public void onResponse(Call<ArrayList<BleDev>> call, Response<ArrayList<BleDev>> response) {
                    if(response.isSuccessful()||response.code()==HTTPCODES.HTTP_CODES_OK){
                        bluetoothViewModel.onRequestPerformed(new ViewModelEvent(BluetoothEventsTypes.AVAILABLE_DEVS,
                                response.body()));
                    } else {
                        bluetoothViewModel.onRequestPerformed(new ViewModelEvent(BluetoothEventsTypes.AVAILABLE_DEVS,
                                null));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BleDev>> call, Throwable t) {
                    bluetoothViewModel.onRequestPerformed(new ViewModelEvent(BluetoothEventsTypes.AVAILABLE_DEVS,
                            null));
                }
            });
        }

    }

    public void sendNewGpsEvent(GpsEvent gpsEvent, PresentationViewModel presentationViewModel){
        if(openedSession!=null){
            Call<Boolean> newGpsCall = krotApi.newgpsevent(openedSession.getApikey(), openedSession.getClient_id(), gpsEvent);
            newGpsCall.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    presentationViewModel.onRequestPerformed(new ViewModelEvent(PresentationEventsTypes.GPS_EVENT,response.body()));
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    presentationViewModel.onRequestPerformed(new ViewModelEvent(PresentationEventsTypes.GPS_EVENT,false));
                }
            });
        }

    }


    public void sendNewBleEvent(BleEvt bleEvt,PresentationViewModel presentationViewModel) {
        if(openedSession!=null){
            Call<Integer> newBLECall = krotApi.newbleevent(openedSession.getApikey(), openedSession.getClient_id(), bleEvt);
            Log.d("status",bleEvt.getScanned().toString());
            newBLECall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    presentationViewModel.onRequestPerformed(new ViewModelEvent(PresentationEventsTypes.BLUETOOTH_EVENT,response.body()));
                }
                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    presentationViewModel.onRequestPerformed(new ViewModelEvent(PresentationEventsTypes.BLUETOOTH_EVENT,null));
                }
            });
        }
    }
}
