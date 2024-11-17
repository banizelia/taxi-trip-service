package com.web.common.exception.trip;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(Long id) {
        super(String.format("Such trip doesn't exist: %d", id));
    }
}