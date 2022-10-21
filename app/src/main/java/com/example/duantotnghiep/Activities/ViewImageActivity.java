package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.R;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ImageView imgViewImage = findViewById(R.id.imgViewImage);
        Intent i = getIntent();
        String url = i.getStringExtra("ViewImage");
        Glide.with(this).load(url).into(imgViewImage);
    }
}