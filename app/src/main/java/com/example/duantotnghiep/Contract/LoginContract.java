package com.example.duantotnghiep.Contract;

import android.content.Context;

import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;

public interface LoginContract {
    interface Model{
        interface  OnFinishedListener{
            void  onFinished(UserResponse userResponse);
            void  onFailure(Throwable t);
        }
        void getLogin(OnFinishedListener onFinishedListener,String phone , String password);
    }
    interface View{
        void startActivity();
        void setUserInfo(User user);
        void showToast(String msg);
        void showProgress();
        void hideProgress();
        void showDialog();
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getCheckLogin(String phone , String password);

    }
}
