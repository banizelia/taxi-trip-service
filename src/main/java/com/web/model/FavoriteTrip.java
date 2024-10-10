package com.web.model;

import jakarta.persistence.*;

/**
 * Model of a favorite trip, containing information about the trip, its position in the favorites list, and the record version.
 */
@Entity
@Table(name = "favorite_trips")
public class FavoriteTrip {

    /**
     * Record version for optimistic locking implementation.
     */
    @Version
    private Long version;

    /**
     * Relationship with the Trip model, representing the trip that is in favorites.
     */
    @OneToOne(mappedBy = "favoriteTrip", fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    /**
     * Favorite trip identifier (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "position")
    private Long position;

    public FavoriteTrip() {
    }

    // Getters and setters

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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