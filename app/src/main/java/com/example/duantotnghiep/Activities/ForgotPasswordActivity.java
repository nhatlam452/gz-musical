package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duantotnghiep.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageView imgBackForgotPassword;
    TextView tvLogInForgotPassword,tvSignUpForgotPassword;
    Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUI();

        imgBackForgotPassword.setOnClickListener(v -> onBackPressed());
        tvLogInForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        tvSignUpForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this,RegisterActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        btnResetPassword.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this,OtpVerifyActivity.class));
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    private void initUI() {
        imgBackForgotPassword = findViewById(R.id.imgBackForgotPassword);
        tvLogInForgotPassword = findViewById(R.id.tvLogInForgotPassword);
        tvSignUpForgotPassword = findViewById(R.id.tvSignUpForgotPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }
}