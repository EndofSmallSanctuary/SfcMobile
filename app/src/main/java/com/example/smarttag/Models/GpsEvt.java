package com.example.smarttag.Models;

import java.util.Date;

public class GpsEvt {
    Long GpsEvt_Time;
    String GpsEvt_TimeStr;
    Float GpsEvt_Lat;
    Float GpsEvt_Long;
    Float GpsEvt_Alt;

    public GpsEvt(Long gpsEvt_Time, String gpsEvt_TimeStr, Float gpsEvt_Lat, Float gpsEvt_Long, Float gpsEvt_Alt) {
        GpsEvt_Time = gpsEvt_Time;
        GpsEvt_TimeStr = new Date(gpsEvt_Time).toString();
        GpsEvt_Lat = gpsEvt_Lat;
        GpsEvt_Long = gpsEvt_Long;
        GpsEvt_Alt = gpsEvt_Alt;
    }

    public Long getGpsEvt_Time() {
        return GpsEvt_Time;
    }

    public String getGpsEvt_TimeStr() {
        return GpsEvt_TimeStr;
    }

    public Float getGpsEvt_Lat() {
        return GpsEvt_Lat;
    }

    public Float getGpsEvt_Long() {
        return GpsEvt_Long;
    }

    public Float getGpsEvt_Alt() {
        return GpsEvt_Alt;
    }
}
