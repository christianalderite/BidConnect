package com.example.leebet_pc.bidconnect;

public class Bid {
    private String username;
    private String title;
    private String views;
    private String timer;
    private String timestamp;
    private String currbid;
    private String buyoutprice;
    private String img_url;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getHighestbid() {
        return highestbid;
    }

    public void setHighestbid(String highestbid) {
        this.highestbid = highestbid;
    }

    private String highestbid;

    public Bid(){
    }

    public Bid(String username, String title, String views, String timer, String timestamp, String currbid, String buyoutprice, String img_url, String status, String highestbid) {
        this.username = username;
        this.title = title;
        this.views = views;
        this.timer = timer;
        this.timestamp = timestamp;
        this.currbid = currbid;
        this.buyoutprice = buyoutprice;
        this.img_url = img_url;
        this.status = status;
        this.highestbid = highestbid;
    }

    public Bid(String username, String title, String views, String timer, String timestamp, String currbid, String buyoutprice, String img_url) {
        this.username = username;
        this.title = title;
        this.views = views;
        this.timer = timer;
        this.timestamp = timestamp;
        this.currbid = currbid;
        this.buyoutprice = buyoutprice;
        this.img_url = img_url;
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

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
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

    public String getCurrbid() {
        return currbid;
    }

    public void setCurrbid(String currbid) {
        this.currbid = currbid;
    }

    public String getBuyoutprice() {
        return buyoutprice;
    }

    public void setBuyoutprice(String buyoutprice) {
        this.buyoutprice = buyoutprice;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
