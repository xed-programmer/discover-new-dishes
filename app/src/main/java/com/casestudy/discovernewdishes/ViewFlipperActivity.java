package com.casestudy.discovernewdishes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.List;

public class ViewFlipperActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewFlipper viewFlipper;
    private TextView et_flip;
    private final int[] imgs = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};
    private final int TIME_OUT = 2000;
    static boolean isRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper);

        viewFlipper = findViewById(R.id.view_flipper);
        et_flip = findViewById(R.id.et_flip);

        viewFlipper.setOnClickListener(this);
        et_flip.setOnClickListener(this);
        Toast.makeText(this, "TAP SCREEN TO SKIP >>", Toast.LENGTH_SHORT).show();
        for(int i : imgs){
            flipImage(i);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, imgs.length * TIME_OUT);
    }
    private void flipImage(int img) {
        Context context = getApplicationContext();
//        RelativeLayout layout = new RelativeLayout(context);
//        TextView txt = new TextView(context);
        ImageView imageView = new ImageView(context);
        imageView.setBackground(getDrawable(R.drawable.bg_success_round));
        RelativeLayout.LayoutParams layoutParams, layoutParams1, layoutParams2 ;
        layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        layoutParams1 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        layoutParams2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        layoutParams2.setMargins(0,0,0,30);

//        txt.setLayoutParams(layoutParams2);
//        txt.setGravity(Gravity.CENTER | Gravity.BOTTOM);
//        txt.setText("TAP SCREEN TO SKIP >>");
        imageView.setLayoutParams(layoutParams);
        imageView.setBackgroundResource(img);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//        layout.setLayoutParams(layoutParams);
//        layout.addView(txt);
//        layout.addView(imageView);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(TIME_OUT);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(context,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(context,android.R.anim.slide_out_right);
    }

    public void openMainActivity() {

        if(!isRunning){
            isRunning = true;
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        openMainActivity();
    }
}