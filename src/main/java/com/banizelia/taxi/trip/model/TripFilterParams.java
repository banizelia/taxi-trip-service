package com.banizelia.taxi.trip.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TripFilterParams {
    private Boolean isFavorite;

    @Parameter(description = "Start date and time of the trip", example = "2000-01-01T00:00:00.000")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime pickupDateTimeFrom = LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0);

    @Parameter(description = "End date and time of the trip", example = "2020-02-01T00:00:00.000")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime pickupDateTimeTo = LocalDateTime.of(2020, 2, 1, 0, 0, 0, 0);

    @Parameter(description = "Minimum wind speed")
    @Min(value = 0)
    private Double minWindSpeed = 0.0;

    @Parameter(description = "Maximum wind speed")
    @Min(value = 0)
    private Double maxWindSpeed = 20.0;


    @AssertTrue()
    private boolean isEndAndStartDateTimeValid() {
        if (pickupDateTimeFrom == null || pickupDateTimeTo == null) {
            return true;
        }
        return pickupDateTimeTo.isAfter(pickupDateTimeFrom) || pickupDateTimeFrom.equals(pickupDateTimeTo);
    }

    @AssertTrue()
    private boolean isMaxAndMinWindSpeedValid() {
        if (minWindSpeed == null || maxWindSpeed == null) {
            return true;
        }
        return maxWindSpeed >= minWindSpeed;
    }
}
