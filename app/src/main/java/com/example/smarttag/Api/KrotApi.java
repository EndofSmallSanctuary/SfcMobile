package com.example.smarttag.Api;

import com.example.smarttag.Models.BleDev;
import com.example.smarttag.Models.BleEvt;
import com.example.smarttag.Models.CltDev;
import com.example.smarttag.Models.DeviceInfo;
import com.example.smarttag.Models.GpsEvent;
import com.example.smarttag.Models.MessageAttachment;
import com.example.smarttag.Models.SfcMessage;
import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.Session;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KrotApi {
    @POST("DevHandshake")
    Call<Session> handshake(@Body DeviceInfo body);

    @POST("PersonalInfo")
    Call<CltDev> personalinfo(@Header("api-key")String apikey,
                          @Query("client_id")Long client_id);

    @POST("Register")
    Call<Void> registration(@Header("api-key")String apikey,
                            @Query("client_id")Long client_id, @Body UserInfo userInfo);

    @POST("AvailableBleDevs")
    Call<ArrayList<BleDev>> availablebledevs(@Header("api-key")String apikey,
                                            @Query("client_id")Long client_id);

    @POST("NewEventGPS")
    Call<Boolean> newgpsevent(@Header("api-key")String apikey,
                              @Query("client_id") Long client_id,
                              @Body GpsEvent gpsEvent);

    @POST("NewEventBLE")
    Call<Integer> newbleevent(@Header("api-key")String apikey,
                              @Query("client_id")Long client_id,
                              @Body BleEvt bleEvt);


    @POST("ChatALL")
    Call<ArrayList<SfcMessage>>  listallchat(@Header("api-key")String apikey,
                                             @Query("client_id")Long client_id);

    @POST("Message")
    Call<Boolean> newmsg(@Header("api-key")String apikey,
                         @Query("client_id")Long client_id,
                         @Body SfcMessage sfcMessage);

    @POST("MessageImage")
    Call<MessageAttachment> msgimg(@Header("api-key")String apikey,
                                   @Query("client_id")Long client_id,
                                   @Query("message_id") Long messageId);
}

