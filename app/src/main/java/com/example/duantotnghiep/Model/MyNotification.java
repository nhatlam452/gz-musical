package com.example.duantotnghiep.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyNotification implements Serializable {
    @SerializedName("message")
    private String body;
    @SerializedName("sentTime")
    private String time;
    private int isViewed;

    public MyNotification(String body, String time) {
        this.body = body;
        this.time = time;
    }

    public MyNotification(String body, String time, int isViewed) {
        this.body = body;
        this.time = time;
        this.isViewed = isViewed;
    }

    public String getBody() {
        return body;
    }

    public String getTime() {
        return time;
    }
    public int isViewed() {
        return isViewed;
    }

    public void setViewed(int viewed) {
        isViewed = viewed;
    }
}

