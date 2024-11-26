package com.web.common.exception.initialization;

public class InvalidTimeZoneException extends RuntimeException {
    public InvalidTimeZoneException(String message) {
        super("Invalid timezone specified: " + message);
    }
}