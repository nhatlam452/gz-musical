package com.example.duantotnghiep.Model;

public class PaymentMethod {
    private int imgPayment;
    private String namePayment;

    public PaymentMethod(int imgPayment, String namePayment) {
        this.imgPayment = imgPayment;
        this.namePayment = namePayment;
    }

    public int getImgPayment() {
        return imgPayment;
    }

    public void setImgPayment(int imgPayment) {
        this.imgPayment = imgPayment;
    }

    public String getNamePayment() {
        return namePayment;
    }

    public void setNamePayment(String namePayment) {
        this.namePayment = namePayment;
    }
}
