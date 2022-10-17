package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;

public class WebViewActivity extends AppCompatActivity {
    private WebView wvBrowser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        AppUtil.showDialog.show(this);
        wvBrowser = findViewById(R.id.wvBrowser);
        WebSettings settings = wvBrowser.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        wvBrowser.loadUrl("https://www.google.com/");
        AppUtil.showDialog.dismiss();
    }
}
