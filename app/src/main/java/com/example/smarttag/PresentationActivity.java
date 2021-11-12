package com.example.smarttag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.smarttag.Views.BluetoothFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PresentationActivity extends AppCompatActivity {

    @BindView(R.id.sfc_navigation_bar)
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        ButterKnife.bind(this);

        //Default fragment - bluetooth ( mAin fucntion)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Presentation_ViewHolder,new BluetoothFragment())
                .commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("Item",item.getItemId()+"");
                switch (item.getItemId()){

                }
                return true;
            }
        });
    }

  }