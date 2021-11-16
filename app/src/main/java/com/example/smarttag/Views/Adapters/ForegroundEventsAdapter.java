package com.example.smarttag.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttag.R;
import com.example.smarttag.ViewModels.BluetoothFragment.ForegroundEvent;

import java.util.ArrayList;

public class ForegroundEventsAdapter extends RecyclerView.Adapter<ForegroundEventsAdapter.ViewHolder> {

    ArrayList<ForegroundEvent> events;
    Context context;

    public ForegroundEventsAdapter(Context context, ArrayList<ForegroundEvent> events) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public ForegroundEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_recycler_node,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForegroundEventsAdapter.ViewHolder holder, int position) {
        holder.status.setImageDrawable(events.get(position).getStatus_icon());
        holder.time.setText(events.get(position).getLocalDateTime());
        holder.header.setText(events.get(position).getEventName());
        holder.description.setText(events.get(position).getEventDesc());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView status;
        TextView header;
        TextView time;
        TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status =  itemView.findViewById(R.id.EventsRecycler_Status);
            header = itemView.findViewById(R.id.EventsRecycler_Name);
            time = itemView.findViewById(R.id.EventsRecycler_Time);
            description = itemView.findViewById(R.id.EventsRecycler_Desc);
        }
    }
}
