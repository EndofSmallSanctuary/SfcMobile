package com.example.smarttag.Views;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smarttag.Models.BleDev;
import com.example.smarttag.PresentationActivity;
import com.example.smarttag.R;
import com.example.smarttag.ViewModels.BluetoothFragment.BluetoothEventsTypes;
import com.example.smarttag.ViewModels.BluetoothFragment.BluetoothViewModel;
import com.example.smarttag.ViewModels.ViewModelEvent;
import com.example.smarttag.Views.Components.PulsatingImage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class BluetoothFragment extends Fragment {

    @BindView(R.id.Bluetooth_Functions_UpdateGps)
    LinearLayout functions_updategps;
    @BindView(R.id.Bluetooth_Functions_ScanNewDevs)
    LinearLayout functions_scannew;
    @BindView(R.id.Bluetooth_Functions_StartRequest)
    LinearLayout functions_request;
    @BindView(R.id.Bluetooth_Functions_StartRequest_Text)
    TextView functions_request_state;

    @BindView(R.id.Bluetooth_Status_Scan)
    ImageView status_scan;
    @BindView(R.id.Bluetooth_Status_BleService)
    ImageView status_bleService;
    @BindView(R.id.Bluetooth_Status_GpsService)
    ImageView status_gpsService;

    PresentationActivity parentActivity;
    Boolean scan_mode = false;
    Boolean isRequestLive = false;
    BluetoothViewModel viewModel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        ButterKnife.bind(this,view);

        parentActivity  =  (PresentationActivity) requireActivity();
        viewModel = new ViewModelProvider(this).get(BluetoothViewModel.class);
        viewModel.getBluetoothLiveData().observe(getViewLifecycleOwner(), new Observer<ViewModelEvent>() {
            @Override
            public void onChanged(ViewModelEvent viewModelEvent) {
                switch (viewModelEvent.getWe_type()){
                    case BluetoothEventsTypes
                            .AVAILABLE_DEVS: {
                        if (viewModelEvent.getObject()!=null){
                            ArrayList<BleDev> bleDevs = (ArrayList<BleDev>) viewModelEvent.getObject();
                            Log.d("dogs",bleDevs.toString()+"");
                            break;
                        }
                    }
                    default:{break;}
                }
            }
        });


        functions_request.setOnClickListener(v->{
            parentActivity.toogleRequest(!isRequestLive);
            updateRequest();
        });

        functions_scannew.setOnClickListener(v -> {
            parentActivity.toogleScanMode(!scan_mode);
           updateScanMode();
        });

        functions_updategps.setOnClickListener(v -> {
            this.updateGpsServiceStatus();
        });

        return view;
    }

    @Override
    public void onResume() {
        viewModel.requestAllBleDevs();
        super.onResume();
    }

    public void updateRequest(){
        if(parentActivity!=null){
            if(parentActivity.getRequestStatus()){
                this.isRequestLive = true;
                this.functions_request_state.setText(R.string.stop_bluetooth_request);
            } else {
                this.isRequestLive = false;
                this.functions_request_state.setText(R.string.start_bluetooth_request);
            }
        }
    }


    public void updateScanMode(){
        if(parentActivity!=null){
            if(parentActivity.getScanStatus()){
                this.scan_mode = true;
                this.status_scan.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_success));
            } else {
                this.scan_mode = false;
                this.status_scan.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_error));
            }
        }
    }

    public void updateGpsServiceStatus(){
        if(parentActivity!=null){
            if(parentActivity.getGpsServiceStatus()){
                this.status_gpsService.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_success));
            } else {
                this.status_gpsService.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_error));
            }
        }
    }

    public void updateBleServiceStatus(){
        if(parentActivity!=null){
            if(parentActivity.getBleServiceStatus()){
                this.status_bleService.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_success));
            } else {
                this.status_bleService.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_error));
            }
        }
    }

    private void stopBLEScan() {
    }


    private void startBLEScan(){
    }
}