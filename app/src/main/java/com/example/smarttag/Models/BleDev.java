package com.example.smarttag.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BleDev implements Parcelable {
    Long idBleDev = 0L;
    String bleDev_MAC;
    String bleDev_Name;
    String bleDev_SerialNumber;

    public BleDev(Long idBleDev, String bleDev_MAC, String bleDev_Name,  String bleDev_SerialNumber) {
        this.idBleDev = idBleDev;
        this.bleDev_MAC = bleDev_MAC;
        this.bleDev_Name = bleDev_Name;
        this.bleDev_SerialNumber = bleDev_SerialNumber;
    }


    public Long getIdBleDev() {
        return idBleDev;
    }

    public void setIdBleDev(Long idBleDev) {
        this.idBleDev = idBleDev;
    }

    public String getBleDev_MAC() {
        return bleDev_MAC;
    }

    public void setBleDev_MAC(String bleDev_MAC) {
        this.bleDev_MAC = bleDev_MAC;
    }

    public String getBleDev_Name() {
        return bleDev_Name;
    }

    public void setBleDev_Name(String bleDev_Name) {
        this.bleDev_Name = bleDev_Name;
    }

    public String getBleDev_SerialNumber() {
        return bleDev_SerialNumber;
    }

    public void setBleDev_SerialNumber(String bleDev_SerialNumber) {
        this.bleDev_SerialNumber = bleDev_SerialNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.idBleDev);
        dest.writeString(this.bleDev_MAC);
        dest.writeString(this.bleDev_Name);
        dest.writeString(this.bleDev_SerialNumber);
    }

    public void readFromParcel(Parcel source) {
        this.idBleDev = source.readLong();
        this.bleDev_MAC = source.readString();
        this.bleDev_Name = source.readString();
        this.bleDev_SerialNumber = source.readString();
    }

    public BleDev() {
    }

    protected BleDev(Parcel in) {
        this.idBleDev = in.readLong();
        this.bleDev_MAC = in.readString();
        this.bleDev_Name = in.readString();
        this.bleDev_SerialNumber = in.readString();
    }

    public static final Parcelable.Creator<BleDev> CREATOR = new Parcelable.Creator<BleDev>() {
        @Override
        public BleDev createFromParcel(Parcel source) {
            return new BleDev(source);
        }

        @Override
        public BleDev[] newArray(int size) {
            return new BleDev[size];
        }
    };

    @Override
    public String toString() {
        return "BleDev{" +
                "idBleDev=" + idBleDev +
                ", bleDev_MAC='" + bleDev_MAC + '\'' +
                ", bleDev_Name='" + bleDev_Name + '\'' +
                ", bLeDev_SerialNumber='" + bleDev_SerialNumber + '\'' +
                '}';
    }
}
