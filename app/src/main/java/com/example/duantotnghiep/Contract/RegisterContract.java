package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;

public interface RegisterContract {
    interface Model{
        interface  OnFinishedListener{
            void  onFinished(UserResponse userResponse);
            void  onFailure(Throwable t);
        }
        void getRegister(OnFinishedListener onFinishedListener,String phone , String password);
    }
    interface View{
        void onSuccess();
        void showToast(String msg);
        void showProgress();
        void hideProgress();
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getCheckRegister(String phone , String password);

    }
}
