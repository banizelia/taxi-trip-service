package com.banizelia.taxi.error.initialization;

public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}