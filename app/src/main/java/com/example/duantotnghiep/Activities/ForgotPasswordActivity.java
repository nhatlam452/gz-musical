package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.Presenter.VerifyOtpInterface;
import com.example.duantotnghiep.Presenter.VerifyOtpPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;

public class ForgotPasswordActivity extends AppCompatActivity implements UserContract.View,VerifyOtpInterface {
    ImageView imgBackForgotPassword;
    VerifyOtpPresenter verifyOtpPresenter;
    TextView tvLogInForgotPassword,tvSignUpForgotPassword;
    Button btnResetPassword;
    EditText edtPhoneNumberForgotPassword;
    UserPresenter userPresenter;
    private final String TAG = "FORGOT_PASSWORD";
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
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        btnResetPassword.setOnClickListener(v -> {
            String phoneNumber = edtPhoneNumberForgotPassword.getText().toString().trim();
            if (AppUtil.ValidateInput.isValidPhoneNumber(phoneNumber)){
                AppUtil.showDialog.show(this);
                userPresenter.onCheckExits(phoneNumber);
            }else {
                Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        verifyOtpPresenter = new VerifyOtpPresenter(this);
        userPresenter = new UserPresenter(this);
        imgBackForgotPassword = findViewById(R.id.imgBackForgotPassword);
        tvLogInForgotPassword = findViewById(R.id.tvLogInForgotPassword);
        tvSignUpForgotPassword = findViewById(R.id.tvSignUpForgotPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        edtPhoneNumberForgotPassword = findViewById(R.id.edtPhoneNumberForgotPassword);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }

    @Override
    public void onSuccess(User user) {
       verifyOtpPresenter.sendOtp(edtPhoneNumberForgotPassword.getText().toString(),this,null);
    }

    @Override
    public void onFail(String msg) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFail(Throwable t) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Error : " +t.getMessage());
    }

    @Override
    public void onSendOtpSuccess(User user, String id) {
        AppUtil.showDialog.dismiss();
        Intent i = new Intent(this,OtpVerifyActivity.class);
        i.putExtra("isForgotPassword",true);
        i.putExtra("userPhone",edtPhoneNumberForgotPassword.getText().toString());
        i.putExtra("verificationId",id);
        startActivity(i);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }

    @Override
    public void onSendOtpFailed(String msg) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}