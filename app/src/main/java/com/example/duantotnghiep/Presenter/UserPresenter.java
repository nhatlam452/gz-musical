package com.example.duantotnghiep.Presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.duantotnghiep.Contract.LoginContract;
import com.example.duantotnghiep.Contract.RegisterContract;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Service.UserService;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class UserPresenter implements LoginContract.Presenter, LoginContract.Model.OnFinishedListener, RegisterContract.Model.OnFinishedRegisterListener, RegisterContract.Presenter {

    private final String TAG = "USER PRESENTER";
    private  FirebaseAuth mAuth ;
    private LoginContract.View loginView;
    private LoginContract.Model model;
    private RegisterContract.View registerView;
    private RegisterContract.Model registerModel;


    public UserPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        model = new UserService();
    }

    public UserPresenter(RegisterContract.View registerView) {
        this.registerView = registerView;
        registerModel = new UserService();
    }

    @Override
    public void getCheckLogin(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty()) {
            loginView.onLoginFail("Phone Number or Password is Empty");
        } else if (!AppUtil.ValidateInput.isValidPhoneNumber(phone)) {
            loginView.onLoginFail("Invalid Phone Number");
        }
        loginView.showProgress();
        model.getLogin(this, phone, password);
    }


    @Override
    public void onLoginFinished(UserResponse userResponse) {
        loginView.hideProgress();
        if (userResponse.getResponseCode() == 1) {
            loginView.onLoginSuccess(userResponse.getData().get(0));
        } else {
            loginView.onLoginFail("Wrong Phone Number or Password");
            Log.d(TAG, "Login code : " + userResponse.getResponseCode() + "Login Msg : " + userResponse.getMessage());
        }

    }

    @Override
    public void onLoginFailure(Throwable t) {
        loginView.onResponseFail(t);
    }

    @Override
    public void onRegisterFinished(int code, String msg) {
        if (code == 1) {
            registerView.onRegisterSuccess();
        } else {
            registerView.onRegisterFail("Register is not Success");
        }
        Log.d(TAG, "Code : " + code + "---- Msg : " + msg);

    }

    @Override
    public void onRegisterFailure(Throwable t) {
        Log.d(TAG, t.getMessage());

    }

    @Override
    public void getCheckRegister(User user, String otp, String id, Activity activity) {
        if (user == null) {
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp);
        signInWithPhoneAuthCredential(credential,user,this,activity);


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, User user, RegisterContract.Model.OnFinishedRegisterListener onFinishedRegisterListener,Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerModel.getRegister(onFinishedRegisterListener,user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("========>", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }
}
