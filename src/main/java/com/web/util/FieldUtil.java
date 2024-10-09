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
 * Утилитарный класс для получения полей, помеченных аннотацией @Column,
 * из моделей Trip, FavoriteTrip и Weather.
 */
public class FieldUtil {

    // Кэшированные наборы полей для разных моделей
    private static final Set<String> TRIP_FIELDS = new HashSet<>();
    private static final Set<String> FAVORITE_TRIP_FIELDS = new HashSet<>();
    private static final Set<String> WEATHER_FIELDS = new HashSet<>();

    /**
     * Возвращает набор полей модели Trip, помеченных аннотацией @Column.
     *
     * @return Set с именами полей Trip
     */
    public static Set<String> getTripFields() {
        // Инициализация кэшированного набора, если он пуст
        if (TRIP_FIELDS.isEmpty()){
            TRIP_FIELDS.addAll(Arrays.stream(Trip.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }

        return TRIP_FIELDS;
    }

    /**
     * Возвращает набор полей модели Weather, помеченных аннотацией @Column.
     *
     * @return Set с именами полей Weather
     */
    public static Set<String> getWeatherFields() {
        // Инициализация кэшированного набора, если он пуст
        if (WEATHER_FIELDS.isEmpty()){
            WEATHER_FIELDS.addAll(Arrays.stream(Weather.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }

        return WEATHER_FIELDS;
    }

    /**
     * Возвращает набор полей модели FavoriteTrip, помеченных аннотацией @Column.
     *
     * @return Set с именами полей FavoriteTrip
     */
    public static Set<String> getFavoriteTripFields() {
        // Инициализация кэшированного набора, если он пуст
        if (FAVORITE_TRIP_FIELDS.isEmpty()){
            FAVORITE_TRIP_FIELDS.addAll(Arrays.stream(FavoriteTrip.class.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(Field::getName)
                    .collect(Collectors.toSet()));
        }

        return FAVORITE_TRIP_FIELDS;
    }
}
