package com.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    @OneToOne(mappedBy = "favoriteTrip")
    private Trip trip;

    @Id
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "position")
    private Long position;

    public FavoriteTrip() {
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
}
