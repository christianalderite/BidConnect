package com.example.leebet_pc.bidconnect;

public class User {
    private String fullname;
    private String username;
    private float rating;
    private String address;
    private String joindate;
    private String uid;
    private String photourl;


    public User(String fullname, String username, float rating, String address, String joindate) {
        this.fullname = fullname;
        this.username = username;
        this.rating = rating;
        this.address = address;
        this.joindate = joindate;
    }

    public User(){}

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }
}
