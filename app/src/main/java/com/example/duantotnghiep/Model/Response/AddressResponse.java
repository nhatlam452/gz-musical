package com.example.duantotnghiep.Model.Response;

import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Model.UserAddress;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressResponse {
    @SerializedName("respone_code")
    private int responseCode;
    private String error;
    private String message;
    private List<UserAddress> data;

    public AddressResponse(int responseCode, String error, String message, List<UserAddress> data) {
        this.responseCode = responseCode;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserAddress> getData() {
        return data;
    }

    public void setData(List<UserAddress> data) {
        this.data = data;
    }
}
