package com.example.smarttag.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smarttag.RetrofitRepositories.KrotRepository;
import com.example.smarttag.Session;

public class WelcomeViewModel extends ViewModel {
    MutableLiveData<Session> sessionLiveData = new MutableLiveData<>();
    KrotRepository krotRepository = KrotRepository.getInstance();

    public void startSesion(){
         krotRepository.startSession(this);
    }

    public MutableLiveData<Session> getSessionLiveData() {
        return sessionLiveData;
    }

    public void onSessionRequestPerformed(Session body) {
        if(body!=null){
            sessionLiveData.postValue(body);
        } else {
            sessionLiveData.postValue(
                    new Session()
            );

        }
    }
}
