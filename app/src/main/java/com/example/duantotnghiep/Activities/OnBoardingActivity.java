package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import com.example.duantotnghiep.Adapter.OnBoardingAdapter;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager2 vpOnBoardingScreen;
    private List<Photo> mListPhoto;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = vpOnBoardingScreen.getCurrentItem();
            if (currentPosition == mListPhoto.size() -1){
                vpOnBoardingScreen.setCurrentItem(0);
            }else {
                vpOnBoardingScreen.setCurrentItem(currentPosition +1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_on_boarding_screen);

        vpOnBoardingScreen = findViewById(R.id.vpOnBoardingScreen);
        CircleIndicator3 ciOnBoardingScreen = findViewById(R.id.ciOnBoardingScreen);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        SharedPreferences sharedPreferences = getSharedPreferences("CHECK_FIRST_INSTALL", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        mListPhoto = getListPhoto();
        OnBoardingAdapter onBoardingAdapter = new OnBoardingAdapter(mListPhoto);
        vpOnBoardingScreen.setAdapter(onBoardingAdapter);
        ciOnBoardingScreen.setViewPager(vpOnBoardingScreen);

        vpOnBoardingScreen.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable,3000);
            }
        });

        btnGetStarted.setOnClickListener(v -> {
            editor.putBoolean("isFirstInstall",true);
            editor.apply();
            startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }
    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img_1,"\"If you look deep enough you will see music,the heart of nature being everywhere music\""));
        list.add(new Photo(R.drawable.img_11,"\"We have learned to express the more delicate nuances of feeling by penetrating more deeply into the mysteries of harmony\""));
        list.add(new Photo(R.drawable.img_3,"\"Music is a hidden arithmetic exercise of the soul, which does not know what it is counting\""));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable,2000);
    }
}