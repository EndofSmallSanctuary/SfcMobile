package com.example.smarttag.ViewModels.BluetoothFragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ForegroundEvent {

    public static final int FOREGROUND_EVENT_TYPE_NETWORK = 0;
    public static final int FOREGROUND_EVENT_TYPE_GPS = 1;
    public static final int FOREGROUND_EVENT_TYPE_SCAN = 2;
    public static final int FOREGROUND_EVENT_TYPE_SUCCESS = 3;
//    public static int FOREGROUND_EVENT_TYPE_ = 0;


    Drawable status_icon;
    String localDateTime;
    String  eventName;
    String eventDesc;

    public ForegroundEvent(Drawable status_icon, String eventName, String eventDesc) {
        this.status_icon = status_icon;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm.ss");
        this.localDateTime = dateFormat.format(new Date());
        this.eventName = eventName;
        this.eventDesc = eventDesc;
    }

    public Drawable getStatus_icon() {
        return status_icon;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

}
