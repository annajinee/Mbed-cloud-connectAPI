package com.example.demo.model.dao;

import com.example.demo.model.Bumps;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BumpsRepo extends MongoRepository<Bumps, String> {

    List<Bumps> findAll();

    List<Bumps> findByDateAddedBetween(String fromDate, String toDate);

}