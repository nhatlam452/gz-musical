package com.example.duantotnghiep.Model.Response;

import com.example.duantotnghiep.Model.MyNotification;
import com.example.duantotnghiep.Model.News;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {
    @SerializedName("respone_code")
    private int responseCode;
    private String error;
    private String message;
    private List<MyNotification> data;

    public NotificationResponse(int responseCode, String error, String message, List<MyNotification> data) {
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

    public List<MyNotification> getData() {
        return data;
    }

    public void setData(List<MyNotification> data) {
        this.data = data;
    }
}
