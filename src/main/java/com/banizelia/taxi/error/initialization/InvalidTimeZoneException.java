package com.banizelia.taxi.error.initialization;

public class InvalidTimeZoneException extends RuntimeException {
    public InvalidTimeZoneException(String message) {
        super("Invalid timezone specified: " + message);
    }
}