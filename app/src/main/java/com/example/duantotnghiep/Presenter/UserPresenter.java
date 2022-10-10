package com.example.duantotnghiep.Presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.duantotnghiep.Contract.LoginContract;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Service.UserService;
import com.example.duantotnghiep.Utilities.AppUtil;

public class UserPresenter implements LoginContract.Presenter,LoginContract.Model.OnFinishedListener {
    private LoginContract.View loginView;
    private LoginContract.Model model;

    public UserPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        model = new UserService();
    }

    @Override
    public void getCheckLogin(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty()){
            loginView.showToast("Phone Number or Password is Empty");
        } else if (!AppUtil.ValidateInput.isValidPhoneNumber(phone)) {
            loginView.showToast("Invalid Phone Number");
        }
        loginView.showProgress();
        model.getLogin(this,phone,password);
    }

    @Override
    public void onFinished(UserResponse userResponse) {
        loginView.hideProgress();
        if (userResponse.getResponseCode() == 1){
        loginView.setUserInfo(userResponse.getData().get(0));
        loginView.startActivity();
        }else {
            loginView.showToast("Wrong Phone Number or Password");
        }

    }

    @Override
    public void onFailure(Throwable t) {
        loginView.onResponseFail(t);
    }
}
