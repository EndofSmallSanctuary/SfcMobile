package com.example.smarttag.Views.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttag.Models.BleDev;
import com.example.smarttag.R;

import java.util.ArrayList;

public class BluetoothDevsAdapter extends RecyclerView.Adapter<BluetoothDevsAdapter.ViewHolder> {

    Context context;
    ArrayList<BleDev> devs;


    public BluetoothDevsAdapter(Context context, ArrayList<BleDev> devs){
        this.context = context;
        this.devs = devs;
    }

    @NonNull
    @Override
    public BluetoothDevsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.bluetooth_recycler_node,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDevsAdapter.ViewHolder holder, int position) {
        holder.dev_name.setText(devs.get(position).getBleDev_Name());
        holder.dev_mac.setText(devs.get(position).getBleDev_MAC());
    }

    @Override
    public int getItemCount() {
        return devs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dev_mac;
        TextView dev_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dev_mac = itemView.findViewById(R.id.BluetoothRecycler_MAC);
            dev_name = itemView.findViewById(R.id.BluetoothRecycler_Name);
        }
    }
}
