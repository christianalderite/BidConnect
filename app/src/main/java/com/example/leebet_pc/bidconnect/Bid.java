package com.example.leebet_pc.bidconnect;

public class Bid {
    private String username, title, views, timer, timestamp, currbid, buyoutprice;

    public Bid(){
    }

    public Bid(String username, String title, String views, String timer, String timestamp, String currbid, String buyoutprice) {
        this.username = username;
        this.title = title;
        this.views = views;
        this.timer = timer;
        this.timestamp = timestamp;
        this.currbid = currbid;
        this.buyoutprice = buyoutprice;
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
}
