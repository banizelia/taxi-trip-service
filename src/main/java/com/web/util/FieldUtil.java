package com.web.util;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import com.web.model.Weather;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldUtil {

    // лучше кэшировать поля и каждый раз вызывать метод?
    private static final Set<String> ALLOWED_FIELDS = new HashSet<>();

    public static Set<String> getAllAllowedFields() {
        if (ALLOWED_FIELDS.isEmpty()){
            ALLOWED_FIELDS.addAll(getTripFields());
            ALLOWED_FIELDS.addAll(getWeatherFields());
            ALLOWED_FIELDS.addAll(getFavoriteTripFields());
        }
        
        return ALLOWED_FIELDS;
    }
    //можно сделать данные поля public, так как в будущем может понадобится проверять поля не только для trips, актаульный вопрос для всех методов
    private static Set<String> getTripFields() {
        return Arrays.stream(Trip.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    private static Set<String> getWeatherFields() {
        return Arrays.stream(Weather.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    private static Set<String> getFavoriteTripFields() {
        return Arrays.stream(FavoriteTrip.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
    }
}
