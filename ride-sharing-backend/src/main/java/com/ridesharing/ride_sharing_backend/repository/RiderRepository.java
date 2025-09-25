package com.ridesharing.ride_sharing_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ridesharing.ride_sharing_backend.entity.Rider;

public interface RiderRepository extends JpaRepository<Rider, Integer> {
    List<Rider> findByRequested(Boolean requested);
}
