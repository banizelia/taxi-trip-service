package com.web.trip.model;

import com.web.common.FieldNamesExtractor;
import com.web.weather.model.WeatherDto;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public record TripFilterParams(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Double minWindSpeed,
        Double maxWindSpeed,
        Integer page,
        Integer size,
        String sort,
        String direction
) {
    public Pageable getPageable() {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        return PageRequest.of(page, size, sortOrder);
    }

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