package com.ridesharing.ride_sharing_backend.dto;

public class MatchResultDTO {
    private Integer driverId;
    private Integer driverLocation;
    private Integer riderId;
    private Integer pickupLocation;
    private Integer dropLocation;
    private Integer totalCost;
    private String description;
    
    // Constructors
    public MatchResultDTO() {}

    public MatchResultDTO(Integer driverId, Integer driverLocation, Integer riderId, 
                          Integer pickupLocation, Integer dropLocation, Integer totalCost, String description) {
        this.driverId = driverId;
        this.driverLocation = driverLocation;
        this.riderId = riderId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.totalCost = totalCost;
        this.description = description;
    }
    
    // Getters and Setters
    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(Integer driverLocation) {
        this.driverLocation = driverLocation;
    }

    public Integer getRiderId() {
        return riderId;
    }

    public void setRiderId(Integer riderId) {
        this.riderId = riderId;
    }

    public Integer getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Integer pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Integer getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(Integer dropLocation) {
        this.dropLocation = dropLocation;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "MatchResultDTO{" +
                "driverId=" + driverId +
                ", driverLocation=" + driverLocation +
                ", riderId=" + riderId +
                ", pickupLocation=" + pickupLocation +
                ", dropLocation=" + dropLocation +
                ", totalCost=" + totalCost +
                ", description='" + description + '\'' +
                '}';
    }
}
