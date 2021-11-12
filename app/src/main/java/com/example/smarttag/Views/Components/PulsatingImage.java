package com.example.smarttag.Views.Components;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class PulsatingImage extends androidx.appcompat.widget.AppCompatImageView {

    private Handler animationHandler = new Handler();

    private Runnable animation = new Runnable() {
        @Override
        public void run() {
            PulsatingImage.this.animate()
                    .scaleX(2f)
                    .scaleY(2f)
                    .alpha(0f)
                    .setDuration(700)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            PulsatingImage.this.setScaleX(1f);
                            PulsatingImage.this.setScaleY(1f);
                            PulsatingImage.this.setAlpha(1f);
                        }
                    });
            animationHandler.postDelayed(this,800);
        }
    };
    public PulsatingImage(@NonNull Context context) {
        super(context);
    }

    public PulsatingImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PulsatingImage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startCustomAnimation(){
        this.animation.run();
    }

    public void stopCustomAnimation(){
        animationHandler.removeCallbacks(animation);
    }
}
