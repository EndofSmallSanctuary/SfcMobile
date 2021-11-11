package com.example.smarttag.ViewModels.WelcomeScreen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smarttag.Models.UserInfo;
import com.example.smarttag.RetrofitRepositories.KrotRepository;
import com.example.smarttag.Session;

public class WelcomeViewModel extends ViewModel {
    MutableLiveData<WelcomeEvent> sessionLiveData = new MutableLiveData<>();
    KrotRepository krotRepository = KrotRepository.getInstance();

    public void startSesion(){
         krotRepository.startSession(this);
    }

    public MutableLiveData<WelcomeEvent> getSessionLiveData() {
        return sessionLiveData;
    }

    public void registrateSomebruh(UserInfo userInfo){
        krotRepository.registerUserInfo(userInfo,this);
    }

    public void onRequestPerformed(WelcomeEvent body) {
        if(body!=null){
            sessionLiveData.postValue(body);
        } else {
            sessionLiveData.postValue(
                    new WelcomeEvent(
                            WelcomeEventsTypes.SESSION_EVENT,
                            new Session()
                    )
            );
        }
    }


}
