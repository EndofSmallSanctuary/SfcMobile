package com.example.smarttag.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.example.smarttag.Models.SfcMessage;
import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.Session;
import com.example.smarttag.ViewModels.SharedViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;

public class WelcomeViewModel extends SharedViewModel {
    public class WelcomeEventsTypes {
        public static final int SESSION_EVENT = 0;
        public static final int REGISTRATION_EVENT = 1;
    }

    public void startSesion(){
         krotRepository.startSession(this);
    }

    public MutableLiveData<ViewModelEvent> getSessionLiveData() {
        return sharedliveData;
    }

    public void registrateSomebruh(UserInfo userInfo){
        krotRepository.registerUserInfo(userInfo,this);
    }

    public void sendNewMessage(SfcMessage message) {
        krotRepository.sendNewMessageWithNoCallback(message);
    }

    @Override
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
