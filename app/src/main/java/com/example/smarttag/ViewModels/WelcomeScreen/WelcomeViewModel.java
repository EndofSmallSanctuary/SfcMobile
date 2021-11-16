package com.example.smarttag.ViewModels.WelcomeScreen;

import androidx.lifecycle.MutableLiveData;

import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.Session;
import com.example.smarttag.ViewModels.SharedViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;

public class WelcomeViewModel extends SharedViewModel {

    public void startSesion(){
         krotRepository.startSession(this);
    }

    public MutableLiveData<ViewModelEvent> getSessionLiveData() {
        return sharedliveData;
    }

    public void registrateSomebruh(UserInfo userInfo){
        krotRepository.registerUserInfo(userInfo,this);
    }

    public void onRequestPerformed(ViewModelEvent body) {
        if(body!=null){
            sharedliveData.postValue(body);
        } else {
            sharedliveData.postValue(
                    new ViewModelEvent(
                            WelcomeEventsTypes.SESSION_EVENT,
                            new Session()
                    )
            );
        }
    }


}
