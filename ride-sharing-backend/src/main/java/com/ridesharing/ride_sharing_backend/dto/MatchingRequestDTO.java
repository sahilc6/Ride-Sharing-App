package com.ridesharing.ride_sharing_backend.dto;

import java.util.List;

import com.ridesharing.ride_sharing_backend.entity.Cost;
import com.ridesharing.ride_sharing_backend.entity.Driver;
import com.ridesharing.ride_sharing_backend.entity.Rider;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MatchingRequestDTO {
    @NotNull(message = "Drivers list cannot be null")
    @NotEmpty(message = "Drivers list cannot be empty")
    @Valid
    private List<Driver> drivers;
    
    @NotNull(message = "Riders list cannot be null")
    @NotEmpty(message = "Riders list cannot be empty") 
    @Valid
    private List<Rider> riders;
    
    @NotNull(message = "Costs list cannot be null")
    @NotEmpty(message = "Costs list cannot be empty")
    @Valid
    private List<Cost> costs;
    
    // Constructors
    public MatchingRequestDTO() {}
    
    public MatchingRequestDTO(List<Driver> drivers, List<Rider> riders, List<Cost> costs) {
        this.drivers = drivers;
        this.riders = riders;
        this.costs = costs;
    }
    
    // Getters and Setters
    public List<Driver> getDrivers() {
        return drivers;
    }
    
    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }
    
    public List<Rider> getRiders() {
        return riders;
    }
    
    public void setRiders(List<Rider> riders) {
        this.riders = riders;
    }
    
    public List<Cost> getCosts() {
        return costs;
    }
    
    public void setCosts(List<Cost> costs) {
        this.costs = costs;
    }
    
    @Override
    public String toString() {
        return "MatchingRequestDTO{" +
                "drivers=" + drivers +
                ", riders=" + riders +
                ", costs=" + costs +
                '}';
    }
}
