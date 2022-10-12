package com.example.duantotnghiep.Model;

public class Photo {
    private int resourceId;
    private String slogan;
    private String url;

    public Photo(int resourceId, String slogan, String url) {
        this.resourceId = resourceId;
        this.slogan = slogan;
        this.url = url;
    }

    public Photo() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
