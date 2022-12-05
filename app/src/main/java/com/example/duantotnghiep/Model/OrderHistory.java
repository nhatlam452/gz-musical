package com.example.duantotnghiep.Model;

import java.util.List;

public class OrderHistory {
    private String Date;
    private List<Order> mList;

    public OrderHistory(String date, List<Order> mList) {
        Date = date;
        this.mList = mList;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public List<Order> getmList() {
        return mList;
    }

    public void setmList(List<Order> mList) {
        this.mList = mList;
    }
}
