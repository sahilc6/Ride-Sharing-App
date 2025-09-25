package com.ridesharing.ride_sharing_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ridesharing.ride_sharing_backend.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
