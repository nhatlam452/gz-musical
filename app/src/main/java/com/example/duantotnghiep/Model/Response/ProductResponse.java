package com.example.duantotnghiep.Model.Response;

import com.example.duantotnghiep.Model.Products;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {
    @SerializedName("respone_code")
    private int responseCode;
    private String error;
    private String message;
    private List<Products> data;

    public ProductResponse() {
    }

    public ProductResponse(int responseCode, String error, String message, List<Products> data) {
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

    public List<Products> getData() {
        return data;
    }

    public void setData(List<Products> data) {
        this.data = data;
    }
}
