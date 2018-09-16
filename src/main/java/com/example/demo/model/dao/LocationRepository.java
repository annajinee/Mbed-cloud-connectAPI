package com.example.demo.model.dao;

import com.example.demo.model.LocationEntity;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationRepository  extends MongoRepository<LocationEntity, String> {

    List<LocationEntity> findBySubjectAndLocationNear(String sid, Point p, Distance d);


}
