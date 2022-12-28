package com.example.duantotnghiep.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User extends UserAddress implements Serializable {
    private String userId;
    private String phoneNumber;
    private String fbId;
    private String email;
    @SerializedName("avatar")
    private String avt;
    private String firstName;
    private String lastName;
    private String password;
    private String dob;
    private String salutation;
    private int notification;
    private int role;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User(String userId, String phoneNumber, String fbId, String email, String avt, String firstName, String lastName, String password, String dob, String salutation, int notification, int role, int state) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.fbId = fbId;
        this.email = email;
        this.avt = avt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.dob = dob;
        this.salutation = salutation;
        this.notification = notification;
        this.role = role;
        this.state = state;
    }

    public User(String fbId, String email, String avt, String firstName, String lastName, String dob, int state) {
        this.fbId = fbId;
        this.email = email;
        this.avt = avt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.state = state;
    }

    public User(String address, String ward, String district, String city, String addressName, String userId, String phoneNumber, String fbId, String email, String avt, String firstName, String lastName, String password, String dob, String salutation, int notification, int role, int state) {
        super(address, ward, district, city, addressName);
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.fbId = fbId;
        this.email = email;
        this.avt = avt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.dob = dob;
        this.salutation = salutation;
        this.notification = notification;
        this.role = role;
        this.state = state;

    }

    public User() {
    }

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserID(String userID) {
        this.userId = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
