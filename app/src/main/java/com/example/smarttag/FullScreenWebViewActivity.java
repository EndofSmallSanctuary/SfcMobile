package com.example.smarttag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

public class FullScreenWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_web_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) findViewById(R.id.SfcView_Holder), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(getIntent().getStringExtra("uri"));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}