package com.example.leebet_pc.bidconnect;

import com.squareup.picasso.MemoryPolicy;

import java.util.Date;

public class Message {
    private String message; // message body
    private String photoUrl;
    private String senderId;
    private String senderFullName;
    private String timestamp;
    private String productId;
    private String productName;
    private boolean belongsToCurrentUser; // is this message sent by us?
    private User user;

    public Message(){

    }

    public Message(String message, String photoUrl, String timestamp, boolean isCurrentUser){
        this.message=message;
        this.photoUrl=photoUrl;
        this.belongsToCurrentUser=isCurrentUser;
        this.timestamp = timestamp;
    }

    public Message (String message, String productId, String productName, String timestamp, boolean isCurrentUser, User user){
        this.message=message;
        this.productId=productId;
        this.belongsToCurrentUser=isCurrentUser;
        this.user=user;
        this.productName = productName;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getmessage() {
        return message;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public User getUser() {
        return user;
    }

    public String getProductId() {
        return productId;
    }

    public String getMessage() {
        return message;
    }

    public String getProductName() {
        return productName;
    }
}