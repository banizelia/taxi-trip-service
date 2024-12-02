package com.banizelia.taxi.error.position;

public class PositionException extends RuntimeException {
    public PositionException(String message) {
        super(message);
    }

    public PositionException(String message, Throwable cause) {
        super(message, cause);
    }
}