package com.banizelia.taxi.error.filter;

import java.time.LocalDateTime;

public class InvalidDateRangeException extends FilterValidationException {
    public InvalidDateRangeException(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(String.format("endDateTime is before startDateTime, startDateTime = %s endDateTime = %s",
                startDateTime, endDateTime));
    }
}