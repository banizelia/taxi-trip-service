package com.web.common.exception.trip;

public class TripAlreadyInFavoritesException extends RuntimeException {
    private final Long tripId;

    public TripAlreadyInFavoritesException(Long tripId) {
        super(String.format("Trip with id %d is already in favorites", tripId));
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }
}