package com.web.common.exception.initialization;

public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}