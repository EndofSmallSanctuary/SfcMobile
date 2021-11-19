package com.example.smarttag.ViewModels.Presentation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.smarttag.Models.BleEvt;
import com.example.smarttag.Models.GpsEvent;
import com.example.smarttag.ViewModels.SharedViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;

import java.util.ArrayList;

public class PresentationViewModel extends SharedViewModel {
    public ArrayList<ForegroundEvent> foregroundEvents = new ArrayList<>();
    private int FOREGROUND_LIMIT = 10;



    public void SendNewGpsEvent(GpsEvent gpsEvent){
        krotRepository.sendNewGpsEvent(gpsEvent,this);
        Log.d("dogs", gpsEvent.getGpsEvt_Time()+" "+ gpsEvent.getGpsEvt_TimeStr());
    }

    public void sendNewBleEvent(BleEvt bleEvt) {
        krotRepository.sendNewBleEvent(bleEvt,this);
    }


    public MutableLiveData<ViewModelEvent> getProcessingEvents(){
        return this.sharedliveData;
    }


    public void onRequestPerformed(ViewModelEvent body) {
        if(body!=null){
            sharedliveData.postValue(body);
        } else {
            //
        }
    }


    public int addNewForegroundEvent(ForegroundEvent event){
        if(foregroundEvents.size()>FOREGROUND_LIMIT){
            foregroundEvents.remove(foregroundEvents.size()-1);
        }
        foregroundEvents.add(0,event);
        return 0;
    }

    public ArrayList<ForegroundEvent> getForegroundEvents() {
        return foregroundEvents;
    }


}
