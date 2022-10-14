package com.example.duantotnghiep.Contract;

import android.app.Activity;

import com.example.duantotnghiep.Model.User;

public interface RegisterContract {
    interface Model{
        interface  OnFinishedRegisterListener{
            void  onRegisterFinished(int code,String msg);
            void  onRegisterFailure(Throwable t);
        }
        void getRegister(OnFinishedRegisterListener onFinishedListener,User user);
    }
    interface View{
        void onRegisterSuccess();
        void onRegisterFail(String msg);
        void showProgress();
        void hideProgress();
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getCheckRegister(User user, String otp, String id, Activity activity);
    }
}
