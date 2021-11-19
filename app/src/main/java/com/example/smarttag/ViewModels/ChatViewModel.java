package com.example.smarttag.ViewModels;

import com.example.smarttag.Models.SfcMessage;

public class ChatViewModel  extends SharedViewModel {
    public class ChatViewModelEventTypes {
        public static final int CHAT_LIST = 0;
        public static final int CHAT_NEW_MSG = 1;
    }

    public void requestChatList(){
        krotRepository.listAllChat(this);
    }

    public void sendManualMesage(SfcMessage sfcMessage) {krotRepository.sendNewMessage(this,sfcMessage);}
}
