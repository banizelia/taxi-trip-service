package com.web.common.exception.filter;

public class InvalidSortDirectionException extends FilterValidationException {
    public InvalidSortDirectionException(String direction) {
        super(String.format("Invalid direction: %s", direction));
    }
}