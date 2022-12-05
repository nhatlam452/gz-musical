package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.duantotnghiep.R;

public class SuccessActivity extends AppCompatActivity {
    Button btnRegisterSuccess;
    TextView tvNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_register_success);

        btnRegisterSuccess = findViewById(R.id.btnRegisterSuccess);
        tvNoti = findViewById(R.id.tvNoti);
        tvNoti.setText(getIntent().getStringExtra("Notification"));
        btnRegisterSuccess.setOnClickListener(v -> {
            if (getIntent().getBooleanExtra("isInMain", false)) {
                startActivity(new Intent(SuccessActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SuccessActivity.this, LoginActivity.class));
            }
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