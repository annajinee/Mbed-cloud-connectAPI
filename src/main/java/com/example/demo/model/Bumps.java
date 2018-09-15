package com.example.demo.model;

/**
 * _id:
 * lat:
 * lon:
 * vel:
 * acc_x:
 * acc_y:
 * acc_z:
 * vehicle:
 * plate:
 * type:
 * cuid: random key
 * dateAdded: datefromat
 *
 *
 */



public class Bumps {

    private String _id;
    private String lat;
    private String lon;
    private String vel;
    private String acc_x;
    private String acc_y;
    private String acc_z;
    private String vehicle;
    private String plate;
    private String type;
    private String cuid;
    private String dateAdded;

    public Bumps() {
    }

    public Bumps(String lat, String lon, String vel, String acc_x, String acc_y, String acc_z, String vehicle, String plate, String type, String cuid, String dateAdded) {
        this.lat = lat;
        this.lon = lon;
        this.vel = vel;
        this.acc_x = acc_x;
        this.acc_y = acc_y;
        this.acc_z = acc_z;
        this.vehicle = vehicle;
        this.plate = plate;
        this.type = type;
        this.cuid = cuid;
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Bumps{" +
                "_id='" + _id + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", vel='" + vel + '\'' +
                ", acc_x='" + acc_x + '\'' +
                ", acc_y='" + acc_y + '\'' +
                ", acc_z='" + acc_z + '\'' +
                ", vehicle='" + vehicle + '\'' +
                ", plate='" + plate + '\'' +
                ", type='" + type + '\'' +
                ", cuid='" + cuid + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getVel() {
        return vel;
    }

    public void setVel(String vel) {
        this.vel = vel;
    }

    public String getAcc_x() {
        return acc_x;
    }

    public void setAcc_x(String acc_x) {
        this.acc_x = acc_x;
    }

    public String getAcc_y() {
        return acc_y;
    }

    public void setAcc_y(String acc_y) {
        this.acc_y = acc_y;
    }

    public String getAcc_z() {
        return acc_z;
    }

    public void setAcc_z(String acc_z) {
        this.acc_z = acc_z;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
