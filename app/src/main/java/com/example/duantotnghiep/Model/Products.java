package com.example.duantotnghiep.Model;


import java.io.Serializable;

public class Products  {
    public static final int TYPE_GRID = 1;
    public static final int TYPE_LIST = 2;

    private int productID;
    private String productName;
    private float price;
    private String url;
    private int discount;
    private String description;
    private int available;
    private int quantity;
    private int typeDisplay;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public Products(int productID, String productName, float price, String url, int discount, String description, int available, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.url = url;
        this.discount = discount;
        this.description = description;
        this.available = available;
        this.quantity = quantity;
    }

    public Products(float price,String productName, String description) {
        this.productName = productName;
        this.price = price;
        this.description = description;
    }

    public Products(int productID, String productName, float price, String url, int discount, String description, int available, int quantity, int typeDisplay) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.url = url;
        this.discount = discount;
        this.description = description;
        this.available = available;
        this.quantity = quantity;
        this.typeDisplay = typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public Products() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
