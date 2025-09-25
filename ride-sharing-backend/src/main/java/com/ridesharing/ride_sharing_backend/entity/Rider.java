package com.ridesharing.ride_sharing_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "riders")
public class Rider {

    @Id
    private Integer id;

    private Integer pickupLocationId;

    private Integer dropLocationId;

    private Boolean requested;  // 1 for yes, 0 for no

    public Rider() {}

    public Rider(Integer id, Integer pickupLocationId, Integer dropLocationId, Boolean requested) {
        this.id = id;
        this.pickupLocationId = pickupLocationId;
        this.dropLocationId = dropLocationId;
        this.requested = requested;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPickupLocationId() { return pickupLocationId; }
    public void setPickupLocationId(Integer pickupLocationId) { this.pickupLocationId = pickupLocationId; }

    public Integer getDropLocationId() { return dropLocationId; }
    public void setDropLocationId(Integer dropLocationId) { this.dropLocationId = dropLocationId; }

    public Boolean getRequested() { return requested; }
    public void setRequested(Boolean requested) { this.requested = requested; }
}
