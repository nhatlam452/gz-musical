package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.duantotnghiep.R;

public class RegisterSuccessActivity extends AppCompatActivity {
    Button btnRegisterSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

        btnRegisterSuccess = findViewById(R.id.btnRegisterSuccess);

        btnRegisterSuccess.setOnClickListener(v -> {
            startActivity(new Intent(RegisterSuccessActivity.this,LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);

    }
}