package com.banizelia.taxi.error.filter;

public class FilterValidationException extends RuntimeException {
    public FilterValidationException(String message) {
        super(message);
    }
}