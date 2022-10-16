package com.example.duantotnghiep.Presenter;

import android.app.Activity;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.duantotnghiep.Model.User;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtpPresenter  {
    private FirebaseAuth mAuth;
    private final VerifyOtpInterface verifyOtpInterface;

    public VerifyOtpPresenter(VerifyOtpInterface verifyOtpInterface) {
        this.verifyOtpInterface = verifyOtpInterface;
    }

    public void sendOtp(String phoneNumber, Activity activity, User user) {
        if (activity == null) {
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        String phone = "+84" + phoneNumber;
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential, activity, user);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("=====>", e + "");
                        verifyOtpInterface.onSendOtpFailed(e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        verifyOtpInterface.onSendOtpSuccess(user, verificationId);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential, Activity activity, User user) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        verifyOtpInterface.onSendOtpSuccess(user, "");
                    } else {
                        Log.d("====>", task.getException() + "");
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            verifyOtpInterface.onSendOtpFailed("The OTP is invalid");
                        }
                    }
                });
    }

}
