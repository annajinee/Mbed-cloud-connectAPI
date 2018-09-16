package com.example.demo.model;


import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
        import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
public class LocationEntity {
    private String id;
    private String subject;
    private GeoJsonPoint location;

    public LocationEntity(final String subject, final GeoJsonPoint location) {
        this.subject = subject;
        this.location = location;
    }


}