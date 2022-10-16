package com.example.duantotnghiep.Presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Service.UserService;
import com.example.duantotnghiep.Utilities.AppUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class UserPresenter implements UserContract.Presenter, UserContract.Model.OnFinishedListener {

    private final String TAG = "USER PRESENTER";
    private final UserContract.View mView;
    private final UserContract.Model model;

    public UserPresenter(UserContract.View mView) {
        this.mView = mView;
        model = new UserService();
    }
    @Override
    public void onLogin(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty()) {
            mView.onFail("Phone Number or Password is Empty");
        } else if (!AppUtil.ValidateInput.isValidPhoneNumber(phone)) {
            mView.onFail("Invalid Phone Number");
        }
        model.getLogin(this, phone, password);
    }


    @Override
    public void onRegister(User user, String otp, String id, Activity activity) {
        if (user == null) {
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp);
        signInWithPhoneAuthCredential(credential,user,this,activity);
    }

    @Override
    public void onCheckExits(String phone) {
        model.checkExitsUser(this,phone);
    }

    @Override
    public void onChangePassword() {

    }

    @Override
    public void onFinished(UserResponse userResponse) {
        if (userResponse.getResponseCode() == 1) {
            if (userResponse.getData() == null){
                mView.onSuccess(null);
                return;
            }
            mView.onSuccess(userResponse.getData().get(0));
        }
        else {
            mView.onFail(userResponse.getMessage());
            Log.d(TAG, " code : " + userResponse.getResponseCode() + " Msg : " + userResponse.getMessage());
        }

    }
    @Override
    public void onFailure(Throwable t) {
        mView.onResponseFail(t);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, User user, UserContract.Model.OnFinishedListener onFinishedListener, Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                            model.getRegister(onFinishedListener,user);
                    } else {
                        Toast.makeText(activity, task.getException() + "", Toast.LENGTH_SHORT).show();
                        // Sign in failed, display a message and update the UI
                        Log.w("========>", "signInWithCredential:failure", task.getException());
                    }
                });
    }
}
