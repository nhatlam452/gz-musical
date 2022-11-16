package com.example.duantotnghiep.Model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    private int commentId;
    private int userId;
    private int productId;
    private String content;
    private String date;
    private String img;
    private int status;
    private String avatar;
    private String name;

    public Comment(int userId, int productId, String content, String date, String img) {
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.date = date;
        this.img = img;
    }

    public Comment(int commentId, int userId, int productId, String content, String date, String img, int status, String avatar, String name) {
        this.commentId = commentId;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.date = date;
        this.img = img;
        this.status = status;
        this.avatar = avatar;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comment(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Comment(int commentId, int userId, int productId, String content, String date, String img, int status) {
        this.commentId = commentId;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.date = date;
        this.img = img;
        this.status = status;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
