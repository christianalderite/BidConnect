package com.example.leebet_pc.bidconnect;

public class ActualBid {

    private String bidderID;
    private String bidID;
    private String auctionID;
    private double bidAmount;

    public ActualBid(String bidID, String auctionID, String bidderID, double bidAmount){
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
