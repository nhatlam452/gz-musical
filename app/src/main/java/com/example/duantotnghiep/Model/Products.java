package com.example.duantotnghiep.Model;

import com.google.gson.annotations.SerializedName;

public class Products {
    private int productID;
    private String productName;
    private String price;
    private String url;

    public Products() {
    }




    public Products(int productID, String productName, String price, String url) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.url = url;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
