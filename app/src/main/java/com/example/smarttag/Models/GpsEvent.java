package com.example.smarttag.Models;

import java.util.Date;

public class GpsEvent {
    Long gpsEvt_Time;
    String gpsEvt_TimeStr;
    double gpsEvt_Lat;
    double gpsEvt_Long;
    double gpsEvt_Alt;

    public GpsEvent(Long gpsEvt_Time, double gpsEvt_Lat, double gpsEvt_Long, double gpsEvt_Alt) {
        this.gpsEvt_Time = gpsEvt_Time;
        gpsEvt_TimeStr = new Date(gpsEvt_Time).toString();
        this.gpsEvt_Lat = gpsEvt_Lat;
        this.gpsEvt_Long = gpsEvt_Long;
        this.gpsEvt_Alt = gpsEvt_Alt;
    }

    public Long getGpsEvt_Time() {
        return gpsEvt_Time;
    }

    public String getGpsEvt_TimeStr() {
        return gpsEvt_TimeStr;
    }

    public double getGpsEvt_Lat() {
        return gpsEvt_Lat;
    }

    public double getGpsEvt_Long() {
        return gpsEvt_Long;
    }

    public double getGpsEvt_Alt() {
        return gpsEvt_Alt;
    }
}
