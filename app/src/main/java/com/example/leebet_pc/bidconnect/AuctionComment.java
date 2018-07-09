package com.example.leebet_pc.bidconnect;

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

    public AuctionComment(String auctionID, String auctionCommentID, String username, String comment) {
        this.auctionID = auctionID;
        this.auctionCommentID = auctionCommentID;
        this.username = username;
        this.comment = comment;
    }

    private String comment;

}