package com.example.leebet_pc.bidconnect;

public class Auction {
    private String auctionID;
    private String username;
    private String title;
    private long views;
    private String timer;
    private String timestamp;
    private double buyoutprice;
    private String img_url;
    private String category;
    private String desc;
    private double minprice;
    private int status;

    public Auction(String auctionID, String username, String title, long views, String timer, String timestamp, double buyoutprice, String img_url, String category, String desc, double minprice, int status) {
        this.auctionID = auctionID;
        this.username = username;
        this.title = title;
        this.views = views;
        this.timer = timer;
        this.timestamp = timestamp;
        this.buyoutprice = buyoutprice;
        this.img_url = img_url;
        this.category = category;
        this.desc = desc;
        this.minprice = minprice;
        this.status = status;
    }

    public Auction(){}

    public String getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(String auctionID) {
        this.auctionID = auctionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getBuyoutprice() {
        return buyoutprice;
    }

    public void setBuyoutprice(double buyoutprice) {
        this.buyoutprice = buyoutprice;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getMinprice() {
        return minprice;
    }

    public void setMinprice(double minprice) {
        this.minprice = minprice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
