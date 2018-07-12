package com.example.leebet_pc.bidconnect;

public class ActualBid {

    private String bidderID;
    private String bidID;

    public void setBidderID(String bidderID) {
        this.bidderID = bidderID;
    }

    public void setBidID(String bidID) {
        this.bidID = bidID;
    }

    public void setAuctionID(String auctionID) {
        this.auctionID = auctionID;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    private String auctionID;
    private double bidAmount;
    public ActualBid(){

    }
    public ActualBid(String auctionID, double bidAmount, String bidID,String bidderID ){
        this.bidID = bidID;
        this.bidAmount = bidAmount;
        this.bidderID = bidderID;
        this.auctionID = auctionID;
    }


    public String getAuctionID() {
        return auctionID;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public String getBidderID() {
        return bidderID;
    }

    public String getBidID() {
        return bidID;
    }
}
