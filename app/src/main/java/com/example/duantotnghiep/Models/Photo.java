package com.example.duantotnghiep.Models;

public class Photo {
    private int resourceId;
    private String slogan;

    public Photo(int resourceId, String slogan) {
        this.resourceId = resourceId;
        this.slogan = slogan;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
