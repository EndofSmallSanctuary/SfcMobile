package com.example.smarttag.Views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarttag.Models.SfcMessage;
import com.example.smarttag.PresentationActivity;
import com.example.smarttag.R;
import com.example.smarttag.ViewModels.ChatViewModel;
import com.example.smarttag.ViewModels.Presentation.ForegroundEvent;
import com.example.smarttag.ViewModels.ViewModelEvent;
import com.example.smarttag.Views.Adapters.ChatMessagesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class ChatFragment extends Fragment {

    SfcMessage messageOnProbation = null;
    String messageAttachment = "";
    PresentationActivity parentActivity;


    ActivityResultLauncher<Intent> attachPickLauncher;

    @BindView(R.id.Chat_RecyclerLoading)
    TextView messagesLoading;

    @BindView(R.id.Chat_AttachPanel)
    ConstraintLayout attachPanel;
    @BindView(R.id.Chat_ControlPanel_FileName)
    TextView filename;
    @BindView(R.id.Chat_ControlPanel_CancelSelection)
    ImageView cancelSelection;

    @BindView(R.id.Chat_Question_Text)
    EditText messageText;
    @BindView(R.id.Chat_InnerRecycler)
    RecyclerView messageRecycler;
    @BindView(R.id.Chat_ControlPanel_Attach)
    ImageView attach;
    @BindView(R.id.Chat_Send)
    FloatingActionButton send;
    ChatViewModel viewModel;
    ArrayList<SfcMessage> sfcMessageArrayList = new ArrayList<>();
    ChatMessagesAdapter chatMessagesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,view);

        messageRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));
        chatMessagesAdapter = new ChatMessagesAdapter(sfcMessageArrayList,getActivity());
        messageRecycler.setAdapter(chatMessagesAdapter);

        viewModel.getSessionLiveData().observe(getViewLifecycleOwner(), new Observer<ViewModelEvent>() {
            @Override
            public void onChanged(ViewModelEvent viewModelEvent) {
                switch (viewModelEvent.getWe_type()){
                    case ChatViewModel.ChatViewModelEventTypes.CHAT_LIST: {
                        if(viewModelEvent.getObject()!=null) {
                            ArrayList<SfcMessage> newMessageIncome = (ArrayList<SfcMessage>) viewModelEvent.getObject();
                            sfcMessageArrayList.clear();
                            sfcMessageArrayList.addAll(newMessageIncome);
                            messagesLoading.setVisibility(View.GONE);
                            chatMessagesAdapter.notifyItemRangeChanged(0,sfcMessageArrayList.size());
                            messageRecycler.setItemViewCacheSize(sfcMessageArrayList.size());
                            messageRecycler.scrollToPosition(sfcMessageArrayList.size()-1);

                        }
                        break;
                    }
                    case ChatViewModel.ChatViewModelEventTypes.CHAT_NEW_MSG:{
                        if(viewModelEvent.getObject()!=null){
                            Boolean actualAnswer = (Boolean) viewModelEvent.getObject();
                            if(actualAnswer){
                                sfcMessageArrayList.add(messageOnProbation);
                                attachPanel.setVisibility(View.GONE);
                                chatMessagesAdapter.notifyItemInserted(sfcMessageArrayList.size()-1);
                                messageRecycler.scrollToPosition(sfcMessageArrayList.size()-1);
                                if(parentActivity!=null){
                                    parentActivity.onNewForegroundEvent(new ForegroundEvent(ContextCompat.getDrawable(requireActivity(),R.drawable.status_success),"Message sent",
                                            getString(R.string.New_Message_success)));
                                }
                            }
                        }
                        break;
                    }
                }
            }


        });

        cancelSelection.setOnClickListener(v->{
            messageAttachment = "";
            attachPanel.setVisibility(View.GONE);
        });

        attach.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            attachPickLauncher.launch(intent);
        });

        send.setOnClickListener(v->{
            String messageCore = messageText.getText().
                    toString()
                    .trim();
            if(!messageCore.equals("")){
                messageOnProbation = new SfcMessage();
                messageOnProbation.setMessage_Type(0);
                messageOnProbation.setMessage_Text(messageCore);
                if(!messageAttachment.equals("")){
                    messageOnProbation.setMessage_Image(messageAttachment);
                }

                viewModel.sendManualMesage(messageOnProbation);
                messageAttachment = "";
                messageText.setText("");

            } else Toasty.error(requireActivity(), getString(R.string.empty_message_body),Toasty.LENGTH_SHORT).show();
        });

        attachPickLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Uri imageuri = result.getData().getData();
                        if(imageuri!=null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageuri);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                                byte[] b64base = outputStream.toByteArray();
                                messageAttachment = Base64.encodeToString(b64base, Base64.DEFAULT);

                                attachPanel.setVisibility(View.VISIBLE);
                                File file = new File(imageuri.getPath());
                                filename.setText(file.getName());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        parentActivity = (PresentationActivity) requireActivity();
        viewModel.requestChatList();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
    }
}