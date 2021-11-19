package com.example.smarttag.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class BleEvt implements Parcelable {
    Long bleEvt_RSSI;
    Long bleEvt_Time;
    String bleEvt_TimeStr;
    double bleEvt_Lat;
    double bleEvt_Long;
    double bleEvt_Alt;
    Long bleEvt_NumMsg;
    BleDev bleDev;
    Boolean scan_mode;

    public BleEvt(Long bleEvt_RSSI, Date time, double bleEvt_Lat, double bleEvt_Long, double bleEvt_Alt, Long bleEvt_NumMsg, Boolean scan_mode) {
        this.bleEvt_RSSI = bleEvt_RSSI;
        this.bleEvt_Time = time.getTime();
        this.bleEvt_TimeStr = time.toString();
        this.bleEvt_Lat = bleEvt_Lat;
        this.bleEvt_Long = bleEvt_Long;
        this.bleEvt_Alt = bleEvt_Alt;
        this.bleEvt_NumMsg = bleEvt_NumMsg;
        this.scan_mode = scan_mode;
    }

    public Long getBleEvt_RSSI() {
        return bleEvt_RSSI;
    }

    public void setBleEvt_RSSI(Long bleEvt_RSSI) {
        this.bleEvt_RSSI = bleEvt_RSSI;
    }

    public Long getBleEvt_Time() {
        return bleEvt_Time;
    }

    public void setBleEvt_Time(Long bleEvt_Time) {
        this.bleEvt_Time = bleEvt_Time;
    }

    public String getBleEvt_TimeStr() {
        return bleEvt_TimeStr;
    }

    public void setBleEvt_TimeStr(String bleEvt_TimeStr) {
        this.bleEvt_TimeStr = bleEvt_TimeStr;
    }

    public double getBleEvt_Lat() {
        return bleEvt_Lat;
    }

    public void setBleEvt_Lat(double bleEvt_Lat) {
        this.bleEvt_Lat = bleEvt_Lat;
    }

    public double getBleEvt_Long() {
        return bleEvt_Long;
    }

    public void setBleEvt_Long(double bleEvt_Long) {
        this.bleEvt_Long = bleEvt_Long;
    }

    public double getBleEvt_Alt() {
        return bleEvt_Alt;
    }

    public void setBleEvt_Alt(double bleEvt_Alt) {
        this.bleEvt_Alt = bleEvt_Alt;
    }

    public Long getBleEvt_NumMsg() {
        return bleEvt_NumMsg;
    }

    public void setBleEvt_NumMsg(Long bleEvt_NumMsg) {
        this.bleEvt_NumMsg = bleEvt_NumMsg;
    }

    public BleDev getBleDev() {
        return bleDev;
    }

    public void setBleDev(BleDev bleDev) {
        this.bleDev = bleDev;
    }

    public Boolean getScanned() {
        return scan_mode;
    }

    public void setScanned(Boolean scanned) {
        scan_mode = scanned;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.bleEvt_RSSI);
        dest.writeValue(this.bleEvt_Time);
        dest.writeString(this.bleEvt_TimeStr);
        dest.writeValue(this.bleEvt_Lat);
        dest.writeValue(this.bleEvt_Long);
        dest.writeValue(this.bleEvt_Alt);
        dest.writeValue(this.bleEvt_NumMsg);
        dest.writeParcelable(this.bleDev, flags);
        dest.writeValue(this.scan_mode);
    }

    public void readFromParcel(Parcel source) {
        this.bleEvt_RSSI = (Long) source.readValue(Long.class.getClassLoader());
        this.bleEvt_Time = (Long) source.readValue(Long.class.getClassLoader());
        this.bleEvt_TimeStr = source.readString();
        this.bleEvt_Lat = (double) source.readValue(double.class.getClassLoader());
        this.bleEvt_Long = (double) source.readValue(double.class.getClassLoader());
        this.bleEvt_Alt = (double) source.readValue(double.class.getClassLoader());
        this.bleEvt_NumMsg = (Long) source.readValue(Long.class.getClassLoader());
        this.bleDev = source.readParcelable(BleDev.class.getClassLoader());
        this.scan_mode = (Boolean) source.readValue(Boolean.class.getClassLoader());
    }

    protected BleEvt(Parcel in) {
        this.bleEvt_RSSI = (Long) in.readValue(Long.class.getClassLoader());
        this.bleEvt_Time = (Long) in.readValue(Long.class.getClassLoader());
        this.bleEvt_TimeStr = in.readString();
        this.bleEvt_Lat = (double) in.readValue(double.class.getClassLoader());
        this.bleEvt_Long = (double) in.readValue(double.class.getClassLoader());
        this.bleEvt_Alt = (double) in.readValue(double.class.getClassLoader());
        this.bleEvt_NumMsg = (Long) in.readValue(Long.class.getClassLoader());
        this.bleDev = in.readParcelable(BleDev.class.getClassLoader());
        this.scan_mode = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<BleEvt> CREATOR = new Creator<BleEvt>() {
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
