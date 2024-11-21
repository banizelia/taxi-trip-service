package com.web.common;

import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ColumnAnnotatedFieldsTest {

    @Test
    void testGetAnnotatedFields_WithColumnAnnotations() {
        Set<String> fields = ColumnAnnotatedFields.getAnnotatedFields(TestClass.class);

        assertAll(
                () -> assertTrue(fields.contains("annotatedField1")),
                () -> assertTrue(fields.contains("annotatedField2")),
                () -> assertTrue(fields.contains("nonAnnotatedField")),
                () -> assertEquals(3, fields.size())
        );
    }

    @Test
    void testGetAnnotatedFields_NullClass() {
        assertThrows(NullPointerException.class,
                () -> ColumnAnnotatedFields.getAnnotatedFields(null));
    }

    @Test
    void testGetAnnotatedFields_NoFields() {
        Set<String> fields = ColumnAnnotatedFields.getAnnotatedFields(EmptyTestClass.class);
        assertEquals(0, fields.size());
    }

    @Test
    void testGetAnnotatedFields_CacheReuse() {
        Set<String> fields1 = ColumnAnnotatedFields.getAnnotatedFields(TestClass.class);
        Set<String> fields2 = ColumnAnnotatedFields.getAnnotatedFields(TestClass.class);

        assertSame(fields1, fields2, "Should return cached result for same class");
    }

    private static class TestClass {
        @Column
        private String annotatedField1;

        @Column
        private int annotatedField2;

        private String nonAnnotatedField;
    }

    private static class EmptyTestClass { }
}