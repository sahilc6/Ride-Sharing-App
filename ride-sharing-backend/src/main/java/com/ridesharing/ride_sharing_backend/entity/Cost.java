package com.ridesharing.ride_sharing_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "costs")
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;        // Auto-generated primary key

    @Column(nullable = false)
    private Integer fromLocation;

    @Column(nullable = false)
    private Integer toLocation;

    @Column(nullable = false)
    private Integer cost;

    // --- Constructors ---
    public Cost() {}

    public Cost(Integer fromLocation, Integer toLocation, Integer cost) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.cost = cost;
    }

    // --- Getters & Setters ---
    public Integer getId() { return id; }

    public Integer getFromLocation() { return fromLocation; }
    public void setFromLocation(Integer fromLocation) { this.fromLocation = fromLocation; }

    public Integer getToLocation() { return toLocation; }
    public void setToLocation(Integer toLocation) { this.toLocation = toLocation; }

    public Integer getCost() { return cost; }
    public void setCost(Integer cost) { this.cost = cost; }
}
