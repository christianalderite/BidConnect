package com.example.leebet_pc.bidconnect;

import com.squareup.picasso.MemoryPolicy;

import java.util.Date;

public class Message {
    private String text; // message body
    private String photoUrl;
    private String senderId;
    private String senderFullName;
    private long timeStamp;
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Message(){

    }

//    public Message(String text, String senderId, String senderFullName, String photoUrl, boolean isCurrentUser) {
//        this.text = text;
//        this.senderId = senderId;
//        this.senderFullName = senderFullName;
//        this.timeStamp = new Date().getTime();
//        this.photoUrl = photoUrl;
//        this.belongsToCurrentUser = belongsToCurrentUser;
//    }

    public Message(String text, String photoUrl, boolean isCurrentUser){
        this.text=text;
        this.photoUrl=photoUrl;
        this.belongsToCurrentUser=isCurrentUser;
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

    public String getText() {
        return text;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}