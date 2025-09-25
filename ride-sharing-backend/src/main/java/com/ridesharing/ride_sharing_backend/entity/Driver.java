package com.ridesharing.ride_sharing_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    private Integer id;

    private Integer locationId;

    private Boolean available;  // 1 for yes, 0 for no

    public Driver() {}

    public Driver(Integer id, Integer locationId, Boolean available) {
        this.id = id;
        this.locationId = locationId;
        this.available = available;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
}
