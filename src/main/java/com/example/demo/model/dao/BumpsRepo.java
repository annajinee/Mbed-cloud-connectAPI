package com.example.demo.model.dao;

import com.example.demo.model.Bumps;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BumpsRepo extends MongoRepository<Bumps, String> {

    List<Bumps> findAll();

    List<Bumps> findByDateAddedBetween(String fromDate, String toDate);

    int deleteByLatIsNull();

    List<Bumps> findByLatBetweenAndLonBetween(String latFrom, String latTo, String lonFrom, String lonTo);


    List<Bumps> findByLonAndLatAndAcc_xAndAcc_yAndAcc_zAndCuid(String lon, String lat, String acc_x, String acc_y, String );
}