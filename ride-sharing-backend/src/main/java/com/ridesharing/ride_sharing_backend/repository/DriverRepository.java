package com.ridesharing.ride_sharing_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ridesharing.ride_sharing_backend.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    List<Driver> findByAvailable(Boolean available);
}
