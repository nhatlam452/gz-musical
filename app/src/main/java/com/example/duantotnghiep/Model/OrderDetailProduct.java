package com.example.duantotnghiep.Model;

public class OrderDetailProduct extends Cart{
    public OrderDetailProduct(int productId, String productName, int quantity, String url, float price, double discount) {
        super(productId, productName, quantity, url, price, discount);
    }
}
