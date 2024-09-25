package com.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    @OneToOne(mappedBy = "favoriteTrip")
    private Trip trip;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @Column(name = "trip_id")
    private Long tripId;

    public FavoriteTrip() {
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
