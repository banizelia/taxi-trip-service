package com.web.trip;

import com.web.common.ColumnAnnotatedFields;
import com.web.common.exception.filter.InvalidDateRangeException;
import com.web.common.exception.filter.InvalidSortDirectionException;
import com.web.common.exception.filter.InvalidSortFieldException;
import com.web.common.exception.filter.InvalidWindSpeedRangeException;
import com.web.trip.model.TripDto;
import com.web.weather.model.WeatherDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public record TripFilterParams(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startDateTime,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endDateTime,

        @Min(0)
        Double minWindSpeed,

        @Min(0)
        Double maxWindSpeed,

        @Min(0)
        Integer page,

        @Min(1)
        @Max(200)
        Integer size,

        String sort,

        String direction
) {
    // Default values
    private static final LocalDateTime DEFAULT_START = LocalDateTime.parse("2016-01-01T00:00:00.000");
    private static final LocalDateTime DEFAULT_END = LocalDateTime.parse("2016-02-01T00:00:00.000");
    private static final double DEFAULT_MIN_WIND = 0.0;
    private static final double DEFAULT_MAX_WIND = 9999.0;
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_DIRECTION = "asc";

    public TripFilterParams {
        if (page == null) page = DEFAULT_PAGE;
        if (size == null) size = DEFAULT_SIZE;
        if (sort == null) sort = DEFAULT_SORT;
        if (direction == null) direction = DEFAULT_DIRECTION;
        if (startDateTime == null) startDateTime = DEFAULT_START;
        if (endDateTime == null) endDateTime = DEFAULT_END;
        if (minWindSpeed == null) minWindSpeed = DEFAULT_MIN_WIND;
        if (maxWindSpeed == null) maxWindSpeed = DEFAULT_MAX_WIND;
    }

    public void validate() {
        validateDateRange();
        validateWindSpeedRange();
        validateSortDirection();
        validateSortField();
    }

    private void validateDateRange() {
        if (endDateTime.isBefore(startDateTime)) {
            throw new InvalidDateRangeException(startDateTime, endDateTime);
        }
    }

    private void validateWindSpeedRange() {
        if (maxWindSpeed <= minWindSpeed) {
            throw new InvalidWindSpeedRangeException(maxWindSpeed, minWindSpeed);
        }
    }

    private void validateSortDirection() {
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new InvalidSortDirectionException(direction);
        }
    }

    private void validateSortField() {
        Set<String> allowedField = new HashSet<>();
        allowedField.addAll(ColumnAnnotatedFields.getAnnotatedFields(TripDto.class));
        allowedField.addAll(ColumnAnnotatedFields.getAnnotatedFields(WeatherDto.class));

        if (!allowedField.contains(sort)) {
            throw new InvalidSortFieldException(sort);
        }
    }
}