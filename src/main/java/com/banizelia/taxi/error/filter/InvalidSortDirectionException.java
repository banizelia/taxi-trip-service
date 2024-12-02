package com.banizelia.taxi.error.filter;

public class InvalidSortDirectionException extends FilterValidationException {
    public InvalidSortDirectionException(String direction) {
        super(String.format("Invalid direction: %s", direction));
    }
}