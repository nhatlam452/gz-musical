package com.example.duantotnghiep.Model;

import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    /*
    * status :
    * 0 : đang chờ xác nhận
    * 1 : đang chuẩn bị đơn hàng
    * 2 : đang giao hàng
    * 3 : đã giao hàng
    * 4 : đã hủy
    * */
    private int status;
    private double total;
    private String note;
    private String createDate;
    private int orderMethod;
    private String dFrom;
    private String dTo;
    private String paymentMethod;
    private List<Cart> listCart;

    public Order(int userId, int status, double total, String note, String createDate, int orderMethod, String dFrom, String dTo, String paymentMethod, List<Cart> listCart) {
        this.userId = userId;
        this.status = status;
        this.total = total;
        this.note = note;
        this.createDate = createDate;
        this.orderMethod = orderMethod;
        this.dFrom = dFrom;
        this.dTo = dTo;
        this.paymentMethod = paymentMethod;
        this.listCart = listCart;
    }

    public Order(int orderId, int userId, int status, double total, String note, String createDate, int orderMethod, String dFrom, String dTo, String paymentMethod, List<Cart> listCart) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.total = total;
        this.note = note;
        this.createDate = createDate;
        this.orderMethod = orderMethod;
        this.dFrom = dFrom;
        this.dTo = dTo;
        this.paymentMethod = paymentMethod;
        this.listCart = listCart;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(int orderMethod) {
        this.orderMethod = orderMethod;
    }

    public String getdFrom() {
        return dFrom;
    }

    public void setdFrom(String dFrom) {
        this.dFrom = dFrom;
    }

    public String getdTo() {
        return dTo;
    }

    public void setdTo(String dTo) {
        this.dTo = dTo;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Cart> getmList() {
        return listCart;
    }

    public void setmList(List<Cart> listCart) {
        this.listCart = listCart;
    }
}
