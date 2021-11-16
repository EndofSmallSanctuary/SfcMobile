package com.example.smarttag.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.smarttag.ViewModels.BluetoothFragment.ForegroundEvent;

import java.util.ArrayList;

public class PresentationViewModel extends SharedViewModel {
    public ArrayList<ForegroundEvent> foregroundEvents = new ArrayList<>();
    private int FOREGROUND_LIMIT = 10;

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
