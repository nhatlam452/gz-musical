package com.example.duantotnghiep.Model;

public class News {
    private int newsId;
    private String title;
    private String image;
    private String url;
    private int type;

    public News(int newsId, String title, String image, String url, int type) {
        this.newsId = newsId;
        this.title = title;
        this.image = image;
        this.url = url;
        this.type = type;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
