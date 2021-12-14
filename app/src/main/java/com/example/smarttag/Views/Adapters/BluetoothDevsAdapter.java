package com.example.smarttag.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttag.BluetoothArmyActivity;
import com.example.smarttag.Models.BleDev;
import com.example.smarttag.R;

import java.util.ArrayList;

public class BluetoothDevsAdapter extends RecyclerView.Adapter<BluetoothDevsAdapter.ViewHolder> {

    Context context;
    BluetoothArmyActivity mappedActivity;
    ArrayList<BleDev> devs;
    Boolean isOnClickRequired;


    public BluetoothDevsAdapter(Context context, ArrayList<BleDev> devs, Boolean isOnClickRequired ){
        this.context = context;
        this.devs = devs;
        this.isOnClickRequired = isOnClickRequired;
        if(isOnClickRequired){
            this.mappedActivity = (BluetoothArmyActivity) context;
        }
    }

    @NonNull
    @Override
    public BluetoothDevsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.bluetooth_recycler_node,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDevsAdapter.ViewHolder holder, int position) {
        String ifSerial;
        if((ifSerial = devs.get(position).getBleDev_SerialNumber())==null||ifSerial.equals("")){
            holder.dev_sn.setText(R.string.serial_not_available);
        } else {
            holder.dev_sn.setText(devs.get(position).getBleDev_SerialNumber());
        }
        holder.dev_mac.setText(devs.get(position).getBleDev_MAC());
        holder.dev_id = devs.get(position).getIdBleDev();
        holder.dev_serial = devs.get(position).getBleDev_SerialNumber();
        holder.itemView.setTag(position);

        if(isOnClickRequired){
            holder.itemView.setOnClickListener(v->{
                mappedActivity.onNewRecyclerItemSelected(holder.itemView.getTag());
            });
        }

    }

    @Override
    public int getItemCount() {
        return devs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dev_mac;
        TextView dev_sn;
        String dev_serial;
        Long dev_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dev_mac = itemView.findViewById(R.id.BluetoothRecycler_MAC);
            dev_sn = itemView.findViewById(R.id.BluetoothRecycler_Serial);
        }
    }
}
