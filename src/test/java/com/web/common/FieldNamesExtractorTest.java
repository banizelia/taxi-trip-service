package com.web.common;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FieldNamesExtractorTest {

    @Test
    void testGetFields_WithColumnAnnotations() {
        Set<String> fields = FieldNamesExtractor.getFields(TestClass.class);

        assertAll(
                () -> assertTrue(fields.contains("annotatedField1")),
                () -> assertTrue(fields.contains("annotatedField2")),
                () -> assertEquals(2, fields.size())
        );
    }

    @Test
    void testGetFields_NullClass() {
        assertThrows(NullPointerException.class,
                () -> FieldNamesExtractor.getFields(null));
    }

    @Test
    void testGetFields_NoFields() {
        Set<String> fields = FieldNamesExtractor.getFields(EmptyTestClass.class);
        assertEquals(0, fields.size());
    }

    @Test
    void testGetFields_CacheReuse() {
        Set<String> fields1 = FieldNamesExtractor.getFields(TestClass.class);
        Set<String> fields2 = FieldNamesExtractor.getFields(TestClass.class);

        assertSame(fields1, fields2, "Should return cached result for same class");
    }

    private static class TestClass {
        private String annotatedField1;
        private int annotatedField2;
    }

    private static class EmptyTestClass { }
}