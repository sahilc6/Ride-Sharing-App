package com.ridesharing.ride_sharing_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridesharing.ride_sharing_backend.entity.Driver;
import com.ridesharing.ride_sharing_backend.entity.Rider;
import com.ridesharing.ride_sharing_backend.repository.DriverRepository;
import com.ridesharing.ride_sharing_backend.repository.RiderRepository;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class DriverRiderController {

    private static final Logger logger = LoggerFactory.getLogger(DriverRiderController.class);

    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;

    @Autowired
    public DriverRiderController(DriverRepository driverRepository, RiderRepository riderRepository) {
        this.driverRepository = driverRepository;
        this.riderRepository = riderRepository;
    }

    @PostMapping("/drivers")
    public ResponseEntity<String> addDriver(@RequestBody Driver driver) {
        try {
            driverRepository.save(driver);
            return ResponseEntity.status(HttpStatus.CREATED).body("Driver added successfully");
        } catch (Exception e) {
            logger.error("Error adding driver: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add driver: " + e.getMessage());
        }
    }

    @PostMapping("/riders")
    public ResponseEntity<String> addRider(@RequestBody Rider rider) {
        try {
            riderRepository.save(rider);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rider added successfully");
        } catch (Exception e) {
            logger.error("Error adding rider: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add rider: " + e.getMessage());
        }
    }
}
