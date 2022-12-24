package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppConstants;
import com.example.duantotnghiep.Utilities.AppUtil;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    boolean isFirstInstall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash_screen);
        SharedPreferences languagePreferences = getSharedPreferences(AppConstants.LANGUAGE,0);
        String dLanguage = languagePreferences.getString("language","en");
        Locale locale = new Locale(dLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences sharedPreferences = getSharedPreferences("CHECK_FIRST_INSTALL",0);
        isFirstInstall = sharedPreferences.getBoolean("isFirstInstall",false);
        loadData();

    }

    private void loadData() {
            new Handler().postDelayed(() -> {
                if (!isFirstInstall){
                    startActivity(new Intent(SplashActivity.this,OnBoardingActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
                finish();
                overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
            },2000);

    }
}