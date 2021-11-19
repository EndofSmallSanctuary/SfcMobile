package com.example.smarttag.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BleDev implements Parcelable {
    String bleDev_MAC;
    String bleDev_Name;
    String bLeDev_SerialNumber;

    public BleDev(String bleDev_MAC, String bleDev_Name,  String bLeDev_SerialNumber) {
        this.bleDev_MAC = bleDev_MAC;
        this.bleDev_Name = bleDev_Name;
        this.bLeDev_SerialNumber = bLeDev_SerialNumber;
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

    public String getbLeDev_SerialNumber() {
        return bLeDev_SerialNumber;
    }

    public void setbLeDev_SerialNumber(String bLeDev_SerialNumber) {
        this.bLeDev_SerialNumber = bLeDev_SerialNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bleDev_MAC);
        dest.writeString(this.bleDev_Name);
        dest.writeString(this.bLeDev_SerialNumber);
    }

    public void readFromParcel(Parcel source) {
        this.bleDev_MAC = source.readString();
        this.bleDev_Name = source.readString();
        this.bLeDev_SerialNumber = source.readString();
    }

    public BleDev() {
    }

    protected BleDev(Parcel in) {
        this.bleDev_MAC = in.readString();
        this.bleDev_Name = in.readString();
        this.bLeDev_SerialNumber = in.readString();
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
}
