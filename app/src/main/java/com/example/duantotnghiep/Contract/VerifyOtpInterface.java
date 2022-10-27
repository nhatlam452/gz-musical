package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Model.UserAddress;

public interface VerifyOtpInterface {
    void onSendOtpSuccess(User user,String id);
    void onSendOtpFailed(String msg);
}
