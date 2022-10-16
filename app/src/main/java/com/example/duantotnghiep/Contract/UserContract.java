package com.example.duantotnghiep.Contract;


import android.app.Activity;

import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;

public interface UserContract {
    interface Model{
        interface  OnFinishedListener{
            void  onFinished(UserResponse userResponse);
            void  onFailure(Throwable t);
        }
        void getLogin(OnFinishedListener onFinishedListener,String phone , String password);
        void getRegister(OnFinishedListener onFinishedListener, User user);
        void checkExitsUser(OnFinishedListener onFinishedListener,String phone);
        void changePassword(OnFinishedListener onFinishedListener,String phone,String password);
    }
    interface View{
        void onSuccess(User user);
        void onFail(String msg);
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void onLogin(String phone , String password);
        void onRegister(User user, String otp, String id, Activity activity);
        void onCheckExits(String phone);
        void onChangePassword();
    }
}
