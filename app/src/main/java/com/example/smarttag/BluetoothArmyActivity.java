package com.example.smarttag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarttag.Models.BleDev;
import com.example.smarttag.Views.Adapters.BluetoothDevsAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothArmyActivity extends AppCompatActivity {


    private String MAP_TAB_LINK_SIGNATURE = "https://sfc.rniirs.ru/map?mobile=heavensnight";
    private String TIMELINE_TAB_LINK_SIGNATURE = "https://sfc.rniirs.ru/Metka?mobile=heavensnight";

    ArrayList<BleDev> devsList =new ArrayList<>();

    @BindView(R.id.DetailedDevs_GoFullScreen)
    Button goFullScreen;

    @BindView(R.id.DetailedDevs_WebLoading)
    SpinKitView webLoading;

    @BindView(R.id.DetailedDevs_WebHolder)
    WebView webView;

    @BindView(R.id.DetailedDevs_TabSelector)
    TabLayout tabLayout;

    @BindView(R.id.DetailedDevs_Recycler)
    RecyclerView recyclerView;
    BluetoothDevsAdapter adapter;

    @BindView(R.id.DetailedDevs_Close)
    ImageView close;

    @BindView(R.id.DetailedDevs_ID)
    TextView chosenDevID;

    @BindView(R.id.DetailedDevs_Name)
    TextView chosenDevName;

    @BindView(R.id.DetailedDevs_MAC)
    TextView chosenDevMac;

    @BindView(R.id.DetailedDevs_Serial)
    TextView chosenDevSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_army);
        ButterKnife.bind(this);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        prepareLinkSignatures();

        close.setOnClickListener(v->{
            onBackPressed();
        });

        adapter = new BluetoothDevsAdapter(this,devsList,true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);
        initWebView();




        Intent contentIntent = getIntent();

       ArrayList<BleDev> tempList = contentIntent.getParcelableArrayListExtra( "devs");
        devsList.addAll(tempList);
        if(devsList!=null&&devsList.size()>0){
            fillDetailedView(devsList.get(0));
            adapter.notifyItemRangeInserted(0,devsList.size());
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Log.d("dogs",MAP_TAB_LINK_SIGNATURE);
                        webView.loadUrl(MAP_TAB_LINK_SIGNATURE);
                        break;
                    case 1:
                        Log.d("dogs",TIMELINE_TAB_LINK_SIGNATURE);
                        webView.loadUrl(TIMELINE_TAB_LINK_SIGNATURE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        goFullScreen.setOnClickListener(v->{
            Intent intent = new Intent(this,FullScreenWebViewActivity.class);
            if(tabLayout.getSelectedTabPosition()==0)
            intent.putExtra("uri",MAP_TAB_LINK_SIGNATURE);
            if(tabLayout.getSelectedTabPosition()==1)
            intent.putExtra("uri",TIMELINE_TAB_LINK_SIGNATURE);
            startActivity(intent);
        });
        webView.loadUrl(MAP_TAB_LINK_SIGNATURE);
    }

    private void initWebView() {
        webView.clearCache(true);


        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new mWebViewClient(webLoading));
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void onNewRecyclerItemSelected(Object tag) {
        fillDetailedView(devsList.get((Integer)tag));
    }


    private class mWebViewClient extends WebViewClient {
        private SpinKitView progressBar;

        public mWebViewClient(SpinKitView progressBar) {
            this.progressBar=progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void fillDetailedView(BleDev bleDev){
        chosenDevID.setText(bleDev.getIdBleDev()+"");
        chosenDevName.setText(bleDev.getBleDev_Name());
        chosenDevMac.setText(bleDev.getBleDev_MAC());
        String serial = bleDev.getBleDev_SerialNumber();
        if(serial==null||serial.equals("")){
            chosenDevSerial.setText(getString(R.string.serial_not_available));
        } else {
            chosenDevSerial.setText(bleDev.getBleDev_SerialNumber());
        }
    }

    private void prepareLinkSignatures(){
        SharedPreferences preferences = getSharedPreferences("prefs",MODE_PRIVATE);
        if(preferences.contains("bleDevs")&&preferences.contains("cltId")){
            StringBuilder builder = new StringBuilder(MAP_TAB_LINK_SIGNATURE);
            builder.append("&idCltDev=").append(preferences.getLong("cltId",0));
            builder.append("&bleDevs=").append(preferences.getString("bleDevs",""));
            MAP_TAB_LINK_SIGNATURE = builder.toString();

            builder = new StringBuilder(TIMELINE_TAB_LINK_SIGNATURE);
            builder.append("&idCltDev=").append(preferences.getLong("cltId",0));
            builder.append("&bleDevs=").append(preferences.getString("bleDevs",""));
            TIMELINE_TAB_LINK_SIGNATURE = builder.toString();
       }
    }
}