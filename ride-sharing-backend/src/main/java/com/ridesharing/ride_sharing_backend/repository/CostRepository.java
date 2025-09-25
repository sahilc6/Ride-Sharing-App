package com.ridesharing.ride_sharing_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ridesharing.ride_sharing_backend.entity.Cost;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    // Optional custom queries:
    // List<Cost> findByFromLocationAndToLocation(Integer fromLocation, Integer toLocation);
}
