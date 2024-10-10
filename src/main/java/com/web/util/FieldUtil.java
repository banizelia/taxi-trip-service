package com.web.util;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import com.web.model.Weather;
import jakarta.persistence.Column;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for retrieving fields annotated with @Column
 * from Trip, FavoriteTrip, and Weather models.
 */
public class FieldUtil {

    // Cached sets of fields for different models
    private static final Set<String> TRIP_FIELDS = new HashSet<>();
    private static final Set<String> FAVORITE_TRIP_FIELDS = new HashSet<>();
    private static final Set<String> WEATHER_FIELDS = new HashSet<>();

    /**
     * Returns a set of Trip model fields annotated with @Column.
     *
     * @return Set of Trip field names
     */
    public static Set<String> getTripFields() {
        // Initialize the cached set if it's empty
        if (TRIP_FIELDS.isEmpty()) {
            TRIP_FIELDS.addAll(Arrays.stream(Trip.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }

        return TRIP_FIELDS;
    }

    /**
     * Returns a set of Weather model fields annotated with @Column.
     *
     * @return Set of Weather field names
     */
    public static Set<String> getWeatherFields() {
        // Initialize the cached set if it's empty
        if (WEATHER_FIELDS.isEmpty()) {
            WEATHER_FIELDS.addAll(Arrays.stream(Weather.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }

        return WEATHER_FIELDS;
    }

    /**
     * Returns a set of FavoriteTrip model fields annotated with @Column.
     *
     * @return Set of FavoriteTrip field names
     */
    public static Set<String> getFavoriteTripFields() {
        // Initialize the cached set if it's empty
        if (FAVORITE_TRIP_FIELDS.isEmpty()) {
            FAVORITE_TRIP_FIELDS.addAll(Arrays.stream(FavoriteTrip.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }

        return FAVORITE_TRIP_FIELDS;
    }
}