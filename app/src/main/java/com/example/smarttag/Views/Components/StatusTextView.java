package com.example.smarttag.Views.Components;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smarttag.R;

public class StatusTextView  extends androidx.appcompat.widget.AppCompatTextView {
    public StatusTextView(@NonNull Context context) {
        super(context);
    }

    public StatusTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void appendNeutral(String content){
        SpannableString spannableString = new SpannableString(content+"\n");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.status_neutral)),0,content.length(),0);
        this.append(spannableString);
    }
    public void appendSuccess(String content){
        SpannableString spannableString = new SpannableString(content+"\n");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.status_success)),0,content.length(),0);
        this.append(spannableString);
    }
    public void appendError(String content){
        SpannableString spannableString = new SpannableString(content+"\n");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.status_error)),0,content.length(),0);
        this.append(spannableString);
    }


}
