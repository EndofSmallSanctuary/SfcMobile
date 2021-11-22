package com.example.smarttag.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smarttag.Models.BleDev;
import com.example.smarttag.PresentationActivity;
import com.example.smarttag.R;
import com.example.smarttag.ViewModels.BluetoothViewModel;
import com.example.smarttag.ViewModels.Presentation.ForegroundEvent;
import com.example.smarttag.ViewModels.ViewModelEvent;
import com.example.smarttag.Views.Adapters.BluetoothDevsAdapter;
import com.example.smarttag.Views.Adapters.ForegroundEventsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BluetoothFragment extends Fragment {

    @BindView(R.id.Bluetooth_Recyclers_Devs)
    RecyclerView devsRecycler;
    BluetoothDevsAdapter devsAdapter;
    ArrayList<BleDev> availableBleDevs = new ArrayList<>();

    @BindView(R.id.Bluetooth_Recyclers_Events)
    RecyclerView foregroundEventsRecycler;
    ForegroundEventsAdapter foregroundEventsAdapter;



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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity  =  (PresentationActivity) requireActivity();
        viewModel = new ViewModelProvider(this).get(BluetoothViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        ButterKnife.bind(this,view);


        foregroundEventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        foregroundEventsAdapter = new ForegroundEventsAdapter(getActivity(), parentActivity.getForegroundEvents());
        foregroundEventsRecycler.setAdapter(foregroundEventsAdapter);

        devsAdapter = new BluetoothDevsAdapter(getActivity(), availableBleDevs);
        devsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        devsRecycler.setAdapter(devsAdapter);

        viewModel.getBluetoothLiveData().observe(getViewLifecycleOwner(), new Observer<ViewModelEvent>() {
            @Override
            public void onChanged(ViewModelEvent viewModelEvent) {
                switch (viewModelEvent.getWe_type()){
                    case BluetoothViewModel.BluetoothEventsTypes
                            .AVAILABLE_DEVS: {
                        if (viewModelEvent.getObject()!=null){
                            ArrayList<BleDev> bleDevs = (ArrayList<BleDev>) viewModelEvent.getObject();
                            if(bleDevs.size()==0){
                                parentActivity.toogleScanMode(true);
                                updateScanMode();
                            }
                            parentActivity.startBluetoothProcessing();
                            availableBleDevs.clear();
                            availableBleDevs.addAll(bleDevs);
                            devsAdapter.notifyDataSetChanged();
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

        functions_updategps.setOnClickListener(v -> this.updateGpsServiceStatus());

        return view;
    }

    @Override
    public void onResume() {
        viewModel.requestAllBleDevs();
        updateScanMode();
        updateRequest();
        updateGpsServiceStatus();
        updateBleServiceStatus();
        super.onResume();
    }

    public void updateRequest(){
        if(parentActivity!=null){
            if(parentActivity.getRequestStatus()){
                this.isRequestLive = true;
                this.functions_request_state.setText(R.string.stop_bluetooth_request);
                parentActivity.prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_NETWORK,getString(R.string.request_enabled),"");
            } else {
                this.isRequestLive = false;
                this.functions_request_state.setText(R.string.start_bluetooth_request);
                parentActivity.prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_NETWORK,getString(R.string.request_disabled),"");
            }
        }
    }


    public void updateScanMode(){
        if(parentActivity!=null){
            if(parentActivity.getScanStatus()){
                this.scan_mode = true;
                this.status_scan.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_success));
                parentActivity.prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_SCAN,getString(R.string.scan_mode_enabled),"");
            } else {
                this.scan_mode = false;
                this.status_scan.setImageDrawable(ContextCompat.getDrawable(parentActivity,R.drawable.status_error));
                parentActivity.prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_SCAN,getString(R.string.scan_mode_disabled),"");

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
            parentActivity.prepareSignatureRequest(ForegroundEvent.FOREGROUND_EVENT_TYPE_GPS,getString(R.string.gps_status_requested),"");

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




    public void onNewForegroundEvent(){
        foregroundEventsAdapter.notifyItemRangeChanged(0,parentActivity.getForegroundEvents().size());
        foregroundEventsRecycler.scrollToPosition(0);
    }


    public void onNewDeviceAllowed() {
        this.viewModel.requestAllBleDevs();
    }
}