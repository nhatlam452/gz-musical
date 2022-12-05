package com.example.duantotnghiep.Model;

import java.util.Date;

public class ChatMsg {
    private String id_received,id_send,message;
    private String  datetime;

    public String getId_received() {
        return id_received;
    }

    public String getId_send() {
        return id_send;
    }

    public String getMessage() {
        return message;
    }

    public String getDatetime() {
        return datetime;
    }

    public ChatMsg(String id_received, String id_send, String message, String datetime) {
        this.id_received = id_received;
        this.id_send = id_send;
        this.message = message;
        this.datetime = datetime;
    }
}
