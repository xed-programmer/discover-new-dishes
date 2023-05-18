package com.casestudy.discovernewdishes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class SplashScreenActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private ImageView img_logo;
    private final int[] imgs = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};
    private final int TIME_OUT = 2000;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, 2500);

    }

    public void openMainActivity() {
        Intent intent = new Intent(this, ViewFlipperActivity.class);
        startActivity(intent);
        finish();
    }
}