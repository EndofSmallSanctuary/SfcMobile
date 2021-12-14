package com.example.smarttag.Models;

import java.util.Date;

public class SfcMessage {

    Long idMessage;
    Long message_CltDev;
    Long message_Time = new Date().getTime();
    double message_Lat = 0;
    double message_Long  = 0 ;
    double message_Alt = 0;
    Integer message_Type;
    String message_Image;
    String message_Text;
    Long message_ReplyTo;

    public SfcMessage(){}

    public SfcMessage(Long idMessage, Long message_CltDev, Long message_Time, double message_Lat, double message_Long, double message_Alt, Integer message_Type, String message_Image, String message_Text, Long message_ReplyTo) {
        this.idMessage = idMessage;
        this.message_CltDev = message_CltDev;
        this.message_Time = message_Time;
        this.message_Lat = message_Lat;
        this.message_Long = message_Long;
        this.message_Alt = message_Alt;
        this.message_Type = message_Type;
        this.message_Image = message_Image;
        this.message_Text = message_Text;
        this.message_ReplyTo = message_ReplyTo;
    }

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public Long getMessage_CltDev() {
        return message_CltDev;
    }

    public void setMessage_CltDev(Long message_CltDev) {
        this.message_CltDev = message_CltDev;
    }

    public Long getMessage_Time() {
        return message_Time;
    }

    public void setMessage_Time(Long message_Time) {
        this.message_Time = message_Time;
    }

    public double getMessage_Lat() {
        return message_Lat;
    }

    public void setMessage_Lat(double message_Lat) {
        this.message_Lat = message_Lat;
    }

    public double getMessage_Long() {
        return message_Long;
    }

    public void setMessage_Long(double message_Long) {
        this.message_Long = message_Long;
    }

    public double getMessage_Alt() {
        return message_Alt;
    }

    public void setMessage_Alt(double message_Alt) {
        this.message_Alt = message_Alt;
    }

    public Integer getMessage_Type() {
        return message_Type;
    }

    public void setMessage_Type(Integer message_Type) {
        this.message_Type = message_Type;
    }

    public String getMessage_Image() {
        return message_Image;
    }

    public void setMessage_Image(String message_Image) {
        this.message_Image = message_Image;
    }

    public String getMessage_Text() {
        return message_Text;
    }

    public void setMessage_Text(String message_Text) {
        this.message_Text = message_Text;
    }

    public Long getMessage_ReplyTo() {
        return message_ReplyTo;
    }

    public void setMessage_ReplyTo(Long message_ReplyTo) {
        this.message_ReplyTo = message_ReplyTo;
    }
}
