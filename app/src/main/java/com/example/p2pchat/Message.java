package com.example.p2pchat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message {
    String message;
    String userType;
    String createdAt;

    public Message(String message, String userType){
        this.message = message;
        this.userType = userType;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        createdAt = sdf.format(Calendar.getInstance().getTime());
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserType() {
        return userType;
    }
}
