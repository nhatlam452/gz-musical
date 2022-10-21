package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.User;

public interface VerifyOtpInterface {
    void onSendOtpSuccess(User user,String id);
    void onSendOtpFailed(String msg);
}
