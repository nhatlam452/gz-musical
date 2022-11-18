package com.example.duantotnghiep.Model.Response;

import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.Store;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreResponse {
    @SerializedName("respone_code")
    private int responseCode;
    private String error;
    private String message;
    private List<Store> data;

    public StoreResponse() {
    }

    public StoreResponse(int responseCode, String error, String message, List<Store> data) {
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

    public List<Store> getData() {
        return data;
    }

    public void setData(List<Store> data) {
        this.data = data;
    }
}
