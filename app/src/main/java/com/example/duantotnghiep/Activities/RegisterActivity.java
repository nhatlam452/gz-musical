package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.duantotnghiep.R;

public class RegisterActivity extends AppCompatActivity {
    private ImageView imgBackRegister;
    private Button btnRegister;
    private EditText edtPhoneNumberRegister,edtPasswordRegister,edtConfirmPassword,
            edtFirstName,edtLastName,edtAddress;
    private CheckBox cbRegisterNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        imgBackRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, RegisterSuccessActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    private void initUI() {
        imgBackRegister = findViewById(R.id.imgBackRegister);
        btnRegister = findViewById(R.id.btnRegister);
        edtPhoneNumberRegister = findViewById(R.id.edtPhoneNumberRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtAddress = findViewById(R.id.edtAddress);
        cbRegisterNotification = findViewById(R.id.cbRegisterNotification);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);

    }
}