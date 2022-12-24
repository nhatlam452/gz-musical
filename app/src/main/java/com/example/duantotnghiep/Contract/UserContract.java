package com.example.duantotnghiep.Contract;


import android.app.Activity;

import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;

public interface UserContract {
    interface Model {
        interface OnFinishedListener {
            void onFinished(UserResponse userResponse);

            void onFailure(Throwable t);
        }

        void getLogin(OnFinishedListener onFinishedListener, String phone, String password);

        void getRegister(OnFinishedListener onFinishedListener, User user);

        void getSocialRegister(OnFinishedListener onFinishedListener, User user);

        void checkExitsUser(OnFinishedListener onFinishedListener, String phone);

        void changePassword(OnFinishedListener onFinishedListener, String phone, String newPassword, String password);

        void updateUser(OnFinishedListener onFinishedListener, String avt,String firstName,String lastName,String salutations,String userId);

        void updateNotification(OnFinishedListener onFinishedListener,int notification,String userId);
    }


    interface View {
        void onSuccess(User user);

        void onFail(String msg);

        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void onLogin(String phone, String password);

        void onRegister(User user, String otp, String id, Activity activity);

        void onSocialRegister(User user,Activity activity);


        void onCheckExits(String phone);

        void onUpdateInfo(String avt,String firstName,String lastName,String salutations,String userId);

        void onChangePassword(String phoneNumber, String newPassword, String oldPassword);
        void onUpdateNotification(int notification, String userId);

    }
}
