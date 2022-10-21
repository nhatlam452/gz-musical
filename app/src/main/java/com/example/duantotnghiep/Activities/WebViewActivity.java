package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;

public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        AppUtil.showDialog.show(this);
        WebView wvBrowser = findViewById(R.id.wvBrowser);
        WebSettings settings = wvBrowser.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        Intent i = getIntent();
        String url = i.getStringExtra("URL");
        wvBrowser.loadUrl(url);
        AppUtil.showDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }
}
