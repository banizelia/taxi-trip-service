package com.banizelia.taxi.trip.model;

import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TripFilterParamsTest {

    private final Validator validator;

    public TripFilterParamsTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testDefaultValuesAreValid() {
        TripFilterParams params = new TripFilterParams();
        Set<ConstraintViolation<TripFilterParams>> violations = validator.validate(params);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidpickupDateTimeTo() {
        TripFilterParams params = new TripFilterParams();
        params.setPickupDateTimeFrom(LocalDateTime.of(2023, 1, 1, 0, 0));
        params.setPickupDateTimeTo(LocalDateTime.of(2022, 1, 1, 0, 0));

        Set<ConstraintViolation<TripFilterParams>> violations = validator.validate(params);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidWindSpeedRange() {
        TripFilterParams params = new TripFilterParams();
        params.setMinWindSpeed(15.0);
        params.setMaxWindSpeed(10.0);

        Set<ConstraintViolation<TripFilterParams>> violations = validator.validate(params);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidCustomValues() {
        TripFilterParams params = new TripFilterParams();
        params.setPickupDateTimeFrom(LocalDateTime.of(2023, 1, 1, 0, 0));
        params.setPickupDateTimeTo(LocalDateTime.of(2023, 2, 1, 0, 0));
        params.setMinWindSpeed(5.0);
        params.setMaxWindSpeed(15.0);

        Set<ConstraintViolation<TripFilterParams>> violations = validator.validate(params);

        assertTrue(violations.isEmpty());
    }
}
