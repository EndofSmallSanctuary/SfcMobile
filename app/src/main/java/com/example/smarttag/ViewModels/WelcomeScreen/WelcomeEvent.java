package com.example.smarttag.ViewModels.WelcomeScreen;


public class WelcomeEvent<T> {
    int we_type;
    T object;

    public WelcomeEvent(int we_type, T object) {
        this.we_type = we_type;
        this.object = object;
    }

    public int getWe_type() {
        return we_type;
    }

    public T getObject() {
        return object;
    }
}
