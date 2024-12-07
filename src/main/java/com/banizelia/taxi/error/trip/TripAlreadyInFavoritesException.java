package com.banizelia.taxi.error.trip;

import lombok.Getter;

@Getter
public class TripAlreadyInFavoritesException extends RuntimeException {
    private final Long tripId;

    public TripAlreadyInFavoritesException(Long tripId) {
        super(String.format("Trip with id %d is already in favorites", tripId));
        this.tripId = tripId;
    }
}