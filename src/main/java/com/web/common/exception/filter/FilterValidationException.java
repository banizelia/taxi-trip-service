package com.web.common.exception.filter;

public class FilterValidationException extends RuntimeException {
    public FilterValidationException(String message) {
        super(message);
    }
}