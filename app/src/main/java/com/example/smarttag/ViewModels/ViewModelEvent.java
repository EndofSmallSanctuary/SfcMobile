package com.example.smarttag.ViewModels;


public class ViewModelEvent<T> {
    int we_type;
    T object;

    public ViewModelEvent(int we_type, T object) {
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
