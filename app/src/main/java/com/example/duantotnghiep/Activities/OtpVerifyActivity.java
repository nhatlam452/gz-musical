package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpVerifyActivity extends AppCompatActivity implements UserContract.View {
    private EditText edtOTP1, edtOTP2, edtOTP3, edtOTP4, edtOTP5, edtOTP6;
    private UserPresenter userPresenter;
    private Button btnConfirmOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        initUI();
        User user = (User) getIntent().getSerializableExtra("UserRegister");
        UserAddress address = (UserAddress) getIntent().getSerializableExtra("Address");

        String verificationId = getIntent().getStringExtra("verificationId");

        btnConfirmOtp.setOnClickListener(v -> {
            AppUtil.showDialog.show(this);
            String OTP = edtOTP1.getText().toString() + edtOTP2.getText().toString() + edtOTP3.getText().toString() + edtOTP4.getText().toString() + edtOTP5.getText().toString() + edtOTP6.getText().toString();
            if (OTP.length() != 6) {
                Toast.makeText(getApplicationContext(), "Please fill the OTP ", Toast.LENGTH_LONG).show();
                AppUtil.showDialog.dismiss();
                return;
            }

            if (getIntent().getBooleanExtra("isForgotPassword", false)) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, OTP);
                signInWithPhoneAuthCredential(credential);
            } else {
                userPresenter.onRegister(user, OTP, verificationId, this);
            }

        });
    }


    private void initUI() {
        userPresenter = new UserPresenter(this);
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
    public void onSuccess(User user) {
        Intent i = new Intent(this,SuccessActivity.class);
        i.putExtra("Notification","Thanks for giving us your precious time. Now you are ready for an enjoyable moment.");
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        AppUtil.showDialog.dismiss();
    }

    @Override
    public void onFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d("OtpActivity : ", "Msg : " + msg);
        AppUtil.showDialog.dismiss();
    }

    @Override
    public void onResponseFail(Throwable t) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(this,ChangePasswordActivity.class);
                        i.putExtra("userPhone",getIntent().getStringExtra("userPhone"));
                        startActivity(i);
                        finish();
                        AppUtil.showDialog.dismiss();
                        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);

                    } else {
                        Toast.makeText(this, task.getException() + "", Toast.LENGTH_SHORT).show();
                        // Sign in failed, display a message and update the UI
                        AppUtil.showDialog.dismiss();
                        Log.w("========>", "signInWithCredential:failure", task.getException());
                    }
                });
    }

}