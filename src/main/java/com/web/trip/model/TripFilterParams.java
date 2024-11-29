package com.web.trip.model;

import com.web.common.FieldNamesExtractor;
import com.web.weather.model.WeatherDto;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public record TripFilterParams(
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startDateTime,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endDateTime,

        @NotNull
        @Min(0)
        Double minWindSpeed,

        @NotNull
        @Min(0)
        Double maxWindSpeed,

        @NotNull
        @Min(0)
        Integer page,

        @NotNull
        @Min(1)
        @Max(200)
        Integer size,

        @NotBlank
        String sort,

        @NotBlank
        String direction
) {
    @AssertTrue
    private boolean isEndDateTimeAfterStartDateTime() {
        return endDateTime.isAfter(startDateTime);
    }

    @AssertTrue
    private boolean isMaxWindSpeedGreaterThanMinWindSpeed() {
        return maxWindSpeed > minWindSpeed;
    }

    @AssertTrue
    private boolean isSortFieldValid() {
        Set<String> allowedFields = new HashSet<>();
        allowedFields.addAll(FieldNamesExtractor.getFields(TripDto.class));
        allowedFields.addAll(FieldNamesExtractor.getFields(WeatherDto.class));
        return allowedFields.contains(sort);
    }

    @AssertTrue
    private boolean isDirectionValid() {
        return "asc".equalsIgnoreCase(direction) || "desc".equalsIgnoreCase(direction);
    }
}