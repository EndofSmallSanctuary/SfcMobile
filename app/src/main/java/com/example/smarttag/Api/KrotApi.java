package com.example.smarttag.Api;

import com.example.smarttag.Models.BleDev;
import com.example.smarttag.Models.DeviceInfo;
import com.example.smarttag.Models.GpsEvt;
import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.Session;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KrotApi {
    @POST("DevHandshake")
    Call<Session> handshake(@Body DeviceInfo body);

    @POST("Register")
    Call<Void> registration(@Header("api-key")String apikey,
                            @Query("client_id")Long client_id, @Body UserInfo userInfo);

    @POST("AvailableBleDevs")
    Call<ArrayList<BleDev>> availablebledevs(@Header("api-key")String apikey,
                                            @Query("client_id")Long client_id);

    @POST("NewGpsEvent")
    Call<Boolean> newgpsevents(@Header("api-key")String apikey,
                               @Query("client_id") Long client_id,
                               @Body GpsEvt gpsEvt);
}

