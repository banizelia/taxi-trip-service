package com.web.trip.service.filter;

import com.web.common.FieldNamesExtractor;
import com.web.trip.model.TripDto;
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
    @AssertTrue(message = "endDateTime must be after startDateTime")
    private boolean isEndDateTimeAfterStartDateTime() {
        return endDateTime.isAfter(startDateTime);
    }

    @AssertTrue(message = "maxWindSpeed must be greater than minWindSpeed")
    private boolean isMaxWindSpeedGreaterThanMinWindSpeed() {
        return maxWindSpeed > minWindSpeed;
    }

    @AssertTrue(message = "sort field is invalid")
    private boolean isSortFieldValid() {
        Set<String> allowedFields = new HashSet<>();
        allowedFields.addAll(FieldNamesExtractor.getFields(TripDto.class));
        allowedFields.addAll(FieldNamesExtractor.getFields(WeatherDto.class));
        return allowedFields.contains(sort);
    }

    @AssertTrue(message = "direction must be 'asc' or 'desc'")
    private boolean isDirectionValid() {
        return "asc".equalsIgnoreCase(direction) || "desc".equalsIgnoreCase(direction);
    }
}