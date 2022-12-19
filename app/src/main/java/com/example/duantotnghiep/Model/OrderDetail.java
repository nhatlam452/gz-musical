package com.example.duantotnghiep.Model;

import java.util.List;

public class OrderDetail {
    private int orderDetail;
    private int orderId;
    private double total;
    private int status;
    private String note;
    private String createDate;
    private int orderMethod;
    private String dFrom;
    private String dTo;
    private String paymentMethod;
    private List<OrderDetailProduct> listProduct;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public OrderDetail(int orderId, double total, int status, String note, String createDate, int orderMethod, String dFrom, String dTo, String paymentMethod, List<OrderDetailProduct> listProduct) {
        this.orderId = orderId;
        this.total = total;
        this.status = status;
        this.note = note;
        this.createDate = createDate;
        this.orderMethod = orderMethod;
        this.dFrom = dFrom;
        this.dTo = dTo;
        this.paymentMethod = paymentMethod;
        this.listProduct = listProduct;
    }

    public int getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(int orderDetail) {
        this.orderDetail = orderDetail;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public List<OrderDetailProduct> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<OrderDetailProduct> listProduct) {
        this.listProduct = listProduct;
    }
}
