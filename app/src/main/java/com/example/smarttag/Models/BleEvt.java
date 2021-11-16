package com.example.smarttag.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BleEvt implements Parcelable {
    Long BleEvt_RSSI;
    Long BleEvt_Time;
    Float BleEvt_Lat;
    Float BleEvt_Long;
    Float BleEvt_Alt;
    Long BleEvt_NumMsg;

    public BleEvt(){};

    public BleEvt(Long bleEvt_RSSI, Long bleEvt_Time, Float bleEvt_Lat, Float bleEvt_Long, Float bleEvt_Alt, Long bleEvt_NumMsg) {
        BleEvt_RSSI = bleEvt_RSSI;
        BleEvt_Time = bleEvt_Time;
        BleEvt_Lat = bleEvt_Lat;
        BleEvt_Long = bleEvt_Long;
        BleEvt_Alt = bleEvt_Alt;
        BleEvt_NumMsg = bleEvt_NumMsg;
    }

    public Long getBleEvt_RSSI() {
        return BleEvt_RSSI;
    }

    public void setBleEvt_RSSI(Long bleEvt_RSSI) {
        BleEvt_RSSI = bleEvt_RSSI;
    }

    public Long getBleEvt_Time() {
        return BleEvt_Time;
    }

    public void setBleEvt_Time(Long bleEvt_Time) {
        BleEvt_Time = bleEvt_Time;
    }

    public Float getBleEvt_Lat() {
        return BleEvt_Lat;
    }

    public void setBleEvt_Lat(Float bleEvt_Lat) {
        BleEvt_Lat = bleEvt_Lat;
    }

    public Float getBleEvt_Long() {
        return BleEvt_Long;
    }

    public void setBleEvt_Long(Float bleEvt_Long) {
        BleEvt_Long = bleEvt_Long;
    }

    public Float getBleEvt_Alt() {
        return BleEvt_Alt;
    }

    public void setBleEvt_Alt(Float bleEvt_Alt) {
        BleEvt_Alt = bleEvt_Alt;
    }

    public Long getBleEvt_NumMsg() {
        return BleEvt_NumMsg;
    }

    public void setBleEvt_NumMsg(Long bleEvt_NumMsg) {
        BleEvt_NumMsg = bleEvt_NumMsg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.BleEvt_RSSI);
        dest.writeValue(this.BleEvt_Time);
        dest.writeValue(this.BleEvt_Lat);
        dest.writeValue(this.BleEvt_Long);
        dest.writeValue(this.BleEvt_Alt);
        dest.writeValue(this.BleEvt_NumMsg);
    }

    public void readFromParcel(Parcel source) {
        this.BleEvt_RSSI = (Long) source.readValue(Long.class.getClassLoader());
        this.BleEvt_Time = (Long) source.readValue(Long.class.getClassLoader());
        this.BleEvt_Lat = (Float) source.readValue(Float.class.getClassLoader());
        this.BleEvt_Long = (Float) source.readValue(Float.class.getClassLoader());
        this.BleEvt_Alt = (Float) source.readValue(Float.class.getClassLoader());
        this.BleEvt_NumMsg = (Long) source.readValue(Long.class.getClassLoader());
    }

    protected BleEvt(Parcel in) {
        this.BleEvt_RSSI = (Long) in.readValue(Long.class.getClassLoader());
        this.BleEvt_Time = (Long) in.readValue(Long.class.getClassLoader());
        this.BleEvt_Lat = (Float) in.readValue(Float.class.getClassLoader());
        this.BleEvt_Long = (Float) in.readValue(Float.class.getClassLoader());
        this.BleEvt_Alt = (Float) in.readValue(Float.class.getClassLoader());
        this.BleEvt_NumMsg = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<BleEvt> CREATOR = new Parcelable.Creator<BleEvt>() {
        @Override
        public BleEvt createFromParcel(Parcel source) {
            return new BleEvt(source);
        }

        @Override
        public BleEvt[] newArray(int size) {
            return new BleEvt[size];
        }
    };
}
