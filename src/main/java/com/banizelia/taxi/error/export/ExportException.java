package com.banizelia.taxi.error.export;


public class ExportException extends RuntimeException {
    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}