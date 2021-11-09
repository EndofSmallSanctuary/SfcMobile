package com.example.smarttag.Api;

import com.example.smarttag.Models.DeviceInfo;
import com.example.smarttag.Session;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface KrotApi {
    @POST("/Api/DevHandshake")
    Call<Session> handshake(@Body DeviceInfo body);
}
