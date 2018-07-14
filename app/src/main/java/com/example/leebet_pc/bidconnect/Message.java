package com.example.leebet_pc.bidconnect;

import com.squareup.picasso.MemoryPolicy;

import java.util.Date;

public class Message {
    private String message; // message body
    private String photoUrl;
    private String senderId;
    private String senderFullName;
    private String productId;
    private long timeStamp;
    private boolean belongsToCurrentUser; // is this message sent by us?
    private User user;

    public Message(){

    }

//    public Message(String message, String senderId, String senderFullName, String photoUrl, boolean isCurrentUser) {
//        this.message = message;
//        this.senderId = senderId;
//        this.senderFullName = senderFullName;
//        this.timeStamp = new Date().getTime();
//        this.photoUrl = photoUrl;
//        this.belongsToCurrentUser = belongsToCurrentUser;
//    }

    public Message(String message, String photoUrl, boolean isCurrentUser){
        this.message=message;
        this.photoUrl=photoUrl;
        this.belongsToCurrentUser=isCurrentUser;
    }

    public Message (String message, String senderId, String senderFullName, String photoUrl){
        this.message=message;
        this.senderId=senderId;
        this.senderFullName=senderFullName;
        this.photoUrl=photoUrl;
    }

    public Message (String message, String productId, User user){
        this.message=message;
        this.productId=productId;
        this.user=user;
    }

    public Message (String senderId, String productId, String message){
        this.message=message;
        this.senderId=senderId;
        this.productId=productId;
    }

    public long getTimeStamp() {
        return timeStamp;
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
}