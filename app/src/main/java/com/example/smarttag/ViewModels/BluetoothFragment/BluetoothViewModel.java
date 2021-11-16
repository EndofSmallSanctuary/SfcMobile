package com.example.smarttag.ViewModels.BluetoothFragment;


import android.os.Handler;

import androidx.lifecycle.MutableLiveData;



import com.example.smarttag.ViewModels.SharedViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;

import java.util.ArrayList;

public class BluetoothViewModel extends SharedViewModel {
    private int FOREGROUND_LIMIT = 10;
    public MutableLiveData<ViewModelEvent> getBluetoothLiveData(){
        return sharedliveData;
    }
    public ArrayList<ForegroundEvent> foregroundEvents = new ArrayList<>();

     public void requestAllBleDevs(){
        krotRepository.getAvailableBleDevs(this);
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
