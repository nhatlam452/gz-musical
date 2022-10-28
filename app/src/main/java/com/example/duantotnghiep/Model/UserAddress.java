package com.example.duantotnghiep.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserAddress implements Serializable {
    private String addressId;
    private String address;
    private String ward;
    private String district;
    private String city;
    private String  addressName;
    private String uId;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public UserAddress(String address, String ward, String district, String city, String addressName) {
        this.address = address;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.addressName = addressName;
    }

    public UserAddress(String addressId, String address, String ward, String district, String city, String userId, String addressName) {
        this.addressId = addressId;
        this.address = address;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.uId = userId;
        this.addressName = addressName;
    }

    public UserAddress() {

    }
}
