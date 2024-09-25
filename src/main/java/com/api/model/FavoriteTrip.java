package com.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    @OneToOne(mappedBy = "favoriteTrip")
    private Trip trip;

    @Id
    @Column(name = "trip_id")
    private Long tripId;

    public FavoriteTrip() {
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
