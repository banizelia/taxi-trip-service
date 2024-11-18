package com.web.common;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for retrieving fields annotated with @Column annotation.
 * This class cannot be instantiated.
 */
public final class ColumnAnnotatedFields {

    private static final Map<Class<?>, Set<String>> CACHED_FIELDS = new HashMap<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws IllegalStateException if instantiation is attempted
     */
    private ColumnAnnotatedFields() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }

    /**
     * Returns a set of field names that are annotated with @Column for the given class.
     * Results are cached for subsequent calls with the same class.
     *
     * @param clazz the class to analyze
     * @return an unmodifiable set of annotated field names
     * @throws NullPointerException if clazz is null
     */
    public static Set<String> getAnnotatedFields(Class<?> clazz) {
        Objects.requireNonNull(clazz, "Class parameter cannot be null");

        return CACHED_FIELDS.computeIfAbsent(clazz, k ->
                Arrays.stream(k.getDeclaredFields())
                        .map(Field::getName)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }
}