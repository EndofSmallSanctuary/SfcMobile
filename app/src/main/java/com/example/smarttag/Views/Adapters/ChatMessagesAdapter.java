package com.example.smarttag.Views.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.download.library.AsyncTask;
import com.example.smarttag.Models.SfcMessage;
import com.example.smarttag.R;
import com.example.smarttag.ViewModels.Presentation.ForegroundEvent;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class ChatMessagesAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Handler handler = new Handler(Looper.getMainLooper());


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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case ChatMessageViewTypes.AUTHOR_CLIENT:{
                ClientMsgHolder clientMsgHolder = (ClientMsgHolder) holder;
                clientMsgHolder.client_Msg.setText(messages.get(position).getMessage_Text());
                        if (messages.get(position).getMessage_Image() != null && !messages.get(position).getMessage_Image().equals("")) {
                            if(clientMsgHolder.client_Image.getVisibility()==View.GONE) {
                                asyncImageLoad(messages.get(position).getMessage_Image().trim(),clientMsgHolder);
                            }
                        }


                clientMsgHolder.client_Time.setText(ForegroundEvent.milisToStrDate(messages.get(position).getMessage_Time()));
                break;
            }
            case ChatMessageViewTypes.AUTHOR_ADMIN:{
                AdminMsgHolder adminMsgHolder = (AdminMsgHolder) holder;
                adminMsgHolder.admin_Msg.setText(messages.get(position).getMessage_Text());
                        if (messages.get(position).getMessage_Image() != null && !messages.get(position).getMessage_Image().equals("")) {
                            if(adminMsgHolder.admin_Image.getVisibility()==View.GONE) {
                               asyncImageLoad(messages.get(position).getMessage_Image().trim(),adminMsgHolder);
                            }
                        }


                adminMsgHolder.admin_Time.setText(ForegroundEvent.milisToStrDate(messages.get(position).getMessage_Time()));
                if(messages.get(position).getMessage_ReplyTo()!=null){
                    adminMsgHolder.admin_Reply.setText(context.getString(R.string.Message_Quote)+findReplyInChain(messages.get(position).getMessage_ReplyTo()));
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

        SfcMessage message = messages.get(position);

        if (message.getMessage_CltDev()!=null && (message.getMessage_CltDev() == 0 || message.getMessage_Text().contains("Благодарим за регистрацию")))
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
        ImageView client_Image;
        TextView client_ImageLoadingText;

        public ClientMsgHolder(@NonNull View itemView) {
            super(itemView);
            client_Msg = itemView.findViewById(R.id.Chat_Message_Client_Text);
            client_Time = itemView.findViewById(R.id.Chat_Message_Client_Time);
            client_Image = itemView.findViewById(R.id.Chat_Message_Client_Image);
            client_ImageLoadingText = itemView.findViewById(R.id.Chat_Message_Client_Text_Loading);
        }
    }

    private class AdminMsgHolder extends RecyclerView.ViewHolder{

        TextView admin_Msg;
        TextView admin_Time;
        TextView admin_Reply;
        ConstraintLayout msgBox;
        ImageView admin_Image;
        TextView admin_ImageLoadingText;

        public AdminMsgHolder(@NonNull View itemView) {
            super(itemView);

            admin_Msg = itemView.findViewById(R.id.Chat_Message_Admin_Text);
            admin_Time = itemView.findViewById(R.id.Chat_Message_Admin_Time);
            admin_Reply = itemView.findViewById(R.id.Chat_Holder_AdminReplyTo);
            admin_Image = itemView.findViewById(R.id.Chat_Message_Admin_Image);
            admin_ImageLoadingText = itemView.findViewById(R.id.Chat_Message_Admin_Text_Loading);
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
            if(sfcMessage.getIdMessage().equals(replyTo))
                return ForegroundEvent.milisToStrDate(sfcMessage.getMessage_Time());
        }
        return null;
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private void asyncImageLoad(String image, RecyclerView.ViewHolder container){
        try {
            if(container.getClass()==AdminMsgHolder.class){
                AdminMsgHolder adminMsgHolder = (AdminMsgHolder)container;
                adminMsgHolder.admin_Image.setVisibility(View.VISIBLE);
                adminMsgHolder.admin_ImageLoadingText.setVisibility(View.VISIBLE);
                        String pureString = image.replace("\n","");
                        byte[] decodedString = Base64.getDecoder().decode(pureString);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adminMsgHolder.admin_ImageLoadingText.setVisibility(View.GONE);
                                Glide.with(context).load(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length))
                                .into(adminMsgHolder.admin_Image);
                            }
                        });

            }
            if(container.getClass()==ClientMsgHolder.class){
                ClientMsgHolder clientMsgHolder = (ClientMsgHolder) container;
                clientMsgHolder.client_Image.setVisibility(View.VISIBLE);
                clientMsgHolder.client_ImageLoadingText.setVisibility(View.VISIBLE);

                String pureString = image.replace("\n","");
                byte[] decodedString = Base64.getDecoder().decode(pureString);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        clientMsgHolder.client_ImageLoadingText.setVisibility(View.GONE);
                        Glide.with(context).load(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length))
                                .into(clientMsgHolder.client_Image);
                            }
                });
            }
        } catch (Exception e){
            Log.d("dogs",e.getMessage());
        }

    }
}
