package com.example.smarttag.ViewModels.BluetoothFragment;


import android.os.Handler;

import androidx.lifecycle.MutableLiveData;



import com.example.smarttag.ViewModels.SharedViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;

import java.util.ArrayList;

public class BluetoothViewModel extends SharedViewModel {

    public MutableLiveData<ViewModelEvent> getBluetoothLiveData(){
        return sharedliveData;
    }


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


}
