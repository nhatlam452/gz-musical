package com.example.duantotnghiep.Model.Response;

import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.OrderDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailResponse {
    @SerializedName("respone_code")
    private int responseCode;
    private String error;
    private String message;
    private List<OrderDetail> data;

    public OrderDetailResponse(int responseCode, String error, String message, List<OrderDetail> data) {
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

    public List<OrderDetail> getData() {
        return data;
    }

    public void setData(List<OrderDetail> data) {
        this.data = data;
    }
}
