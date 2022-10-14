package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duantotnghiep.Contract.RegisterContract;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;

public class OtpVerifyActivity extends AppCompatActivity implements RegisterContract.View{
    private EditText edtOTP1, edtOTP2, edtOTP3, edtOTP4, edtOTP5, edtOTP6;
    private UserPresenter userPresenter;
    private Button btnConfirmOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        setContentView(R.layout.activity_otp_verify);
        User user = (User) getIntent().getSerializableExtra("UserRegister");
        String verificationId = getIntent().getStringExtra("verificationId");

        btnConfirmOtp.setOnClickListener(v ->{
            String OTP = edtOTP1.getText().toString() + edtOTP2.getText().toString() + edtOTP3.getText().toString() + edtOTP4.getText().toString() + edtOTP5.getText().toString() + edtOTP6.getText().toString();
            if (OTP.isEmpty())
                Toast.makeText(getApplicationContext(), "Blank Field can not be processed", Toast.LENGTH_LONG).show();
            else if (OTP.length() != 6)
                Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
            else {
                userPresenter.getCheckRegister(user,OTP,verificationId,this);
            }
        });
    }

    private void initUI() {
        userPresenter = new UserPresenter((RegisterContract.View) this);
        btnConfirmOtp = findViewById(R.id.btnConfirmOtp);
        edtOTP1 = findViewById(R.id.edtOTP1);
        edtOTP2 = findViewById(R.id.edtOTP2);
        edtOTP3 = findViewById(R.id.edtOTP3);
        edtOTP4 = findViewById(R.id.edtOTP4);
        edtOTP5 = findViewById(R.id.edtOTP5);
        edtOTP6 = findViewById(R.id.edtOTP6);
        edtOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    edtOTP2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    edtOTP3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    edtOTP4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    edtOTP5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtOTP5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    edtOTP6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);

    }

    @Override
    public void onRegisterSuccess() {
        startActivity(new Intent(this,RegisterSuccessActivity.class));
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        finish();
    }

    @Override
    public void onRegisterFail(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onResponseFail(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}