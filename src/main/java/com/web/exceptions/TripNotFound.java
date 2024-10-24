package com.web.exceptions;

public class TripNotFound extends RuntimeException {
    public TripNotFound(String message) {
        super(message);
    }
}
