package com.example.leebet_pc.bidconnect;

public class ActualBid {

    private String bidderId;
    private String auctionID;
    private Float bidAmount;

    public ActualBid(String bidderId, String auctionID, float bidAmount){
        this.bidAmount = bidAmount;
        this.bidderId = bidderId;
        this.auctionID = auctionID;
    }

    public String getAuctionID() {
        return auctionID;
    }

    public Float getBidAmount() {
        return bidAmount;
    }

    public String getBidderId() {
        return bidderId;
    }


}
