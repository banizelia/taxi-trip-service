package com.web.common.exception.filter;

public class InvalidSortFieldException extends FilterValidationException {
    public InvalidSortFieldException(String sortBy) {
        super(String.format("Invalid sort field: %s", sortBy));
    }
}