package com.web.common.exception.position;

public class PositionException extends RuntimeException {
    public PositionException(String message) {
        super(message);
    }

    public PositionException(String message, Throwable cause) {
        super(message, cause);
    }
}