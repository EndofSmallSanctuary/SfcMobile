package com.example.smarttag.Views;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smarttag.R;
import com.example.smarttag.Views.Components.PulsatingImage;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class BluetoothFragment extends Fragment {

    Boolean inSearch = false;

//    @BindView(R.id.Bluetooth_PulsatingImage)
//    PulsatingImage pulsatingImage;
//    @BindView(R.id.Bluetooth_SearchButton)
//    Button searchButton;
//    @BindView(R.id.Bluetooth_text)
//    TextView bluetooth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        ButterKnife.bind(this,view);

//        searchButton.setOnClickListener(v -> {
//            if(!inSearch) {
//                pulsatingImage.startCustomAnimation();
//                searchButton.setText(R.string.stop);
//                //also to start discovering devices all around
//                startBLEScan();
//            }
//            else {
//                pulsatingImage.stopCustomAnimation();
//                searchButton.setText(R.string.scan);
//                stopBLEScan();
//            }
//            inSearch = !inSearch;
//        });

        return view;
    }

    private void stopBLEScan() {
    }


    private void startBLEScan(){

    }
}