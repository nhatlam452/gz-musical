package com.example.duantotnghiep.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String userId;
    private String phoneNumber;
    private String fbId;
    private String email;
    private String fullname;
    private String firstName;
    private String lastName;
    private String password;
    private String dob;
    private String salutation;
    private String notification;
    private String role;

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User(String userId, String phoneNumber, String fbId, String email, String fullname, String password, String dob, String salutation, String notification, String role) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.fbId = fbId;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.dob = dob;
        this.salutation = salutation;
        this.notification = notification;
        this.role = role;
    }

    public String getUserID() {
        return userId;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
