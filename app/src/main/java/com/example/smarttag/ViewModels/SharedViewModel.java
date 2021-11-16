package com.example.smarttag.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smarttag.RetrofitRepositories.KrotRepository;

public abstract class SharedViewModel extends ViewModel {
    protected    MutableLiveData<ViewModelEvent> sharedliveData = new MutableLiveData<>();
    protected    KrotRepository krotRepository = KrotRepository.getInstance();

    public MutableLiveData<ViewModelEvent> getSessionLiveData() {
        return sharedliveData;
    }

    public void onRequestPerformed(ViewModelEvent body){}


}
