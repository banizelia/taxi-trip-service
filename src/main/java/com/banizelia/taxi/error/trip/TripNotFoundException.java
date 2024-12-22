package com.banizelia.taxi.error.trip;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(Long id) {
        super(String.format("Such trip doesn't exist: %d", id));
    }
}