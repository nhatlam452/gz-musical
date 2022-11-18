package com.example.duantotnghiep.Model;

public class Store {
    private int storeId;
    private String storeName;
    private double latitude;
    private double longitude;
    private String storeAddress;

    public Store(int storeId, String storeName, double latitude, double longitude, String storeAddress) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeAddress = storeAddress;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
}
