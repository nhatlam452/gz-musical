package com.example.duantotnghiep.Model;

import java.util.List;

public class ProductDetail {
    private int productDetailId;
    private String top;
    private String back;
    private String neck;
    private String fingerBoard;
    private String bridge;
    private String origin;
    private List<Images> listImage;

    public ProductDetail(int productDetailId, String top, String back, String neck, String fingerBoard, String bridge, String origin, List<Images> listImage) {
        this.productDetailId = productDetailId;
        this.top = top;
        this.back = back;
        this.neck = neck;
        this.fingerBoard = fingerBoard;
        this.bridge = bridge;
        this.origin = origin;
        this.listImage = listImage;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getFingerBoard() {
        return fingerBoard;
    }

    public void setFingerBoard(String fingerBoard) {
        this.fingerBoard = fingerBoard;
    }

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<Images> getListImage() {
        return listImage;
    }

    public void setListImage(List<Images> listImage) {
        this.listImage = listImage;
    }
}
