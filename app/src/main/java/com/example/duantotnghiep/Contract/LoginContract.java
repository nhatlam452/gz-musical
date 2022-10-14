package com.example.duantotnghiep.Contract;


import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;

public interface LoginContract {
    interface Model{
        interface  OnFinishedListener{
            void  onLoginFinished(UserResponse userResponse);
            void  onLoginFailure(Throwable t);
        }
        void getLogin(OnFinishedListener onFinishedListener,String phone , String password);
    }
    interface View{
        void onLoginSuccess(User user);
        void onLoginFail(String msg);
        void showProgress();
        void hideProgress();
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getCheckLogin(String phone , String password);
    }
}
