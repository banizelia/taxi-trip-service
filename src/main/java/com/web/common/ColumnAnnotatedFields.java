package com.web.common;

import com.web.favorite.model.FavoriteTrip;
import com.web.trip.model.Trip;
import com.web.weather.model.Weather;
import jakarta.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for retrieving fields annotated with @Column
 * from Trip, FavoriteTrip, and Weather models.
 */
public class ColumnAnnotatedFields {
    private static final Set<String> TRIP_FIELDS = new HashSet<>();
    private static final Set<String> FAVORITE_TRIP_FIELDS = new HashSet<>();
    private static final Set<String> WEATHER_FIELDS = new HashSet<>();

    public static Set<String> getTripFields() {
        if (TRIP_FIELDS.isEmpty()) {
            TRIP_FIELDS.addAll(Arrays.stream(Trip.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }
        return TRIP_FIELDS;
    }

    public static Set<String> getWeatherFields() {
        if (WEATHER_FIELDS.isEmpty()) {
            WEATHER_FIELDS.addAll(Arrays.stream(Weather.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }
        return WEATHER_FIELDS;
    }

    public static Set<String> getFavoriteTripFields() {
        if (FAVORITE_TRIP_FIELDS.isEmpty()) {
            FAVORITE_TRIP_FIELDS.addAll(Arrays.stream(FavoriteTrip.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }
        return FAVORITE_TRIP_FIELDS;
    }
}