package com.example.smarttag.Views.Adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttag.Models.SfcMessage;
import com.example.smarttag.R;
import com.example.smarttag.ViewModels.Presentation.ForegroundEvent;

import java.util.ArrayList;
import java.util.Date;

public class ChatMessagesAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private class ChatMessageViewTypes {
        public static final int AUTHOR_CLIENT = 1;
        public static final int AUTHOR_ADMIN = 0;
    }

    private ArrayList<SfcMessage> messages;
    private Context context;

    public ChatMessagesAdapter(ArrayList<SfcMessage> messages, Context context){
        this.messages = messages;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ChatMessageViewTypes.AUTHOR_CLIENT){
            return new ClientMsgHolder(LayoutInflater.from(context).inflate(R.layout.chat_recycler_node_client,parent,false));
        } if (viewType == ChatMessageViewTypes.AUTHOR_ADMIN){
            return new AdminMsgHolder(LayoutInflater.from(context).inflate(R.layout.chat_recycler_node_admin,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case ChatMessageViewTypes.AUTHOR_CLIENT:{
                ClientMsgHolder clientMsgHolder = (ClientMsgHolder) holder;
                clientMsgHolder.client_Msg.setText(messages.get(position).getMessage_Text());
                clientMsgHolder.client_Time.setText(ForegroundEvent.milisToStrDate(messages.get(position).getMessage_Time()));
                break;
            }
            case ChatMessageViewTypes.AUTHOR_ADMIN:{
                AdminMsgHolder adminMsgHolder = (AdminMsgHolder) holder;
                adminMsgHolder.admin_Msg.setText(messages.get(position).getMessage_Text());
                adminMsgHolder.admin_Time.setText(ForegroundEvent.milisToStrDate(messages.get(position).getMessage_Time()));
                if(messages.get(position).getMessage_ReplyTo()!=null){
                    adminMsgHolder.admin_Reply.setText("Reply on message at : "+findReplyInChain(messages.get(position).getMessage_ReplyTo()));
                } else {
                    adminMsgHolder.admin_Reply.setVisibility(View.GONE);

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) adminMsgHolder.msgBox.getLayoutParams();
                    params.topMargin = intToDp(10);
                }
                break;
            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getMessage_CltDev()!=null && messages.get(position).getMessage_CltDev() == 0)
            return  ChatMessageViewTypes.AUTHOR_ADMIN;
        else
            return ChatMessageViewTypes.AUTHOR_CLIENT;
    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    private class ClientMsgHolder extends RecyclerView.ViewHolder {

        TextView client_Msg;
        TextView client_Time;

        public ClientMsgHolder(@NonNull View itemView) {
            super(itemView);

            client_Msg = itemView.findViewById(R.id.Chat_Message_Client_Text);
            client_Time = itemView.findViewById(R.id.Chat_Message_Client_Time);
        }
    }

    private class AdminMsgHolder extends RecyclerView.ViewHolder{

        TextView admin_Msg;
        TextView admin_Time;
        TextView admin_Reply;
        ConstraintLayout msgBox;

        public AdminMsgHolder(@NonNull View itemView) {
            super(itemView);

            admin_Msg = itemView.findViewById(R.id.Chat_Message_Admin_Text);
            admin_Time = itemView.findViewById(R.id.Chat_Message_Admin_Time);
            admin_Reply = itemView.findViewById(R.id.Chat_Holder_AdminReplyTo);
            msgBox = itemView.findViewById(R.id.Chat_Holder_AdminMSG);
        }

    }


    private int intToDp(int sizeInDp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDp, context.getResources()
                        .getDisplayMetrics());
    }

    // T_T----T_T
    private String findReplyInChain(Long replyTo){
        for(int i =0; i < messages.size(); i++){
            SfcMessage sfcMessage = messages.get(i);
            if(sfcMessage.getIdMessage()==replyTo)
                return ForegroundEvent.milisToStrDate(sfcMessage.getMessage_Time());
        }
        return null;
    }


}
