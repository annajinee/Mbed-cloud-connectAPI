package com.example.demo.model;


public class LocationEntry {
    private String lon;
    private String lan;


    public LocationEntry(String lon, String lan) {
        this.lon = lon;
        this.lan = lan;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }
}