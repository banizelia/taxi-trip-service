package com.web.common;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public final class ColumnAnnotatedFields {

    private static final Map<Class<?>, Set<String>> CACHED_FIELDS = new HashMap<>();

    private ColumnAnnotatedFields() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }

    public static Set<String> getAnnotatedFields(Class<?> clazz) {
        Objects.requireNonNull(clazz, "Class parameter cannot be null");

        return CACHED_FIELDS.computeIfAbsent(clazz, k ->
                Arrays.stream(k.getDeclaredFields())
                        .map(Field::getName)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }
}