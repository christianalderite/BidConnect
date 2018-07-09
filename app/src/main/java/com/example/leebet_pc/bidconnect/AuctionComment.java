package com.example.leebet_pc.bidconnect;

import java.text.SimpleDateFormat;
import java.util.Date;
public class AuctionComment {

    private String auctionID;

    public String getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(String auctionID) {
        this.auctionID = auctionID;
    }

    public String getAuctionCommentID() {
        return auctionCommentID;
    }

    public void setAuctionCommentID(String auctionCommentID) {
        this.auctionCommentID = auctionCommentID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String auctionCommentID;
    private String username;
    private String timestamp;
    public AuctionComment(){

    }
    public AuctionComment(String auctionID, String auctionCommentID, String username, String comment) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
        String timestamp = dateFormat.format(new Date());

        this.auctionID = auctionID;
        this.auctionCommentID = auctionCommentID;
        this.username = username;
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public AuctionComment(){}

    private String comment;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
