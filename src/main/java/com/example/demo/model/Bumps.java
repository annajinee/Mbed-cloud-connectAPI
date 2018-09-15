//package com.example.demo.model;
//
//import javax.persistence.*;
//
///**
// * _id:
// * lat:
// * lon:
// * vel:
// * acc_x:
// * acc_y:
// * acc_z:
// * vehicle:
// * plate:
// * type:
// * cuid: random key
// * dateAdded: datefromat
// *
// *
// */
//
//
//
//public class Bumps {
//    @Id
//    private String _id;
//    private String lat;
//    private String lon;
//    private String vel;
//    private String acc_x;
//    private String acc_y;
//    private String acc_z;
//    private String vehicle;
//    private String plate;
//    private String type;
//    private String cuid;
//    private String dateAdded;
//
//    public Bumps() {
//    }
//
//    public Bumps(String lat, String lon, String vel, String acc_x, String acc_y, String acc_z, String vehicle, String plate, String type, String cuid, String dateAdded) {
//        this.lat = lat;
//        this.lon = lon;
//        this.vel = vel;
//        this.acc_x = acc_x;
//        this.acc_y = acc_y;
//        this.acc_z = acc_z;
//        this.vehicle = vehicle;
//        this.plate = plate;
//        this.type = type;
//        this.cuid = cuid;
//        this.dateAdded = dateAdded;
//    }
//
//    @Override
//    public String toString() {
//        return "StTierCodes{" +
//                "_id='" + _id + '\'' +
//                ", lat='" + lat + '\'' +
//                ", lon='" + lon + '\'' +
//                ", vel='" + vel + '\'' +
//                ", acc_x='" + acc_x + '\'' +
//                ", acc_y='" + acc_y + '\'' +
//                ", acc_z='" + acc_z + '\'' +
//                ", vehicle='" + vehicle + '\'' +
//                ", plate='" + plate + '\'' +
//                ", type='" + type + '\'' +
//                ", cuid='" + cuid + '\'' +
//                ", dateAdded='" + dateAdded + '\'' +
//                '}';
//    }
//
//}
