package com.web.common;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
class FieldNamesExtractorTest {

    static class TestClass {
        private String field1;
        private int field2;
        protected double field3;
        public boolean field4;
    }

    static class EmptyClass {
    }

    @Test
    void getFields_shouldReturnFieldNames() {
        Set<String> fieldNames = FieldNamesExtractor.getFields(TestClass.class);

        assertEquals(Set.of("field1", "field2", "field3", "field4"), fieldNames);
    }

    @Test
    void getFields_shouldReturnEmptySetForClassWithoutFields() {
        Set<String> fieldNames = FieldNamesExtractor.getFields(EmptyClass.class);

        assertTrue(fieldNames.isEmpty());
    }

    @Test
    void getFields_shouldThrowExceptionWhenClassIsNull() {
        assertThrows(NullPointerException.class, () -> FieldNamesExtractor.getFields(null));
    }

    @Test
    void getFields_shouldCacheResults() {
        Set<String> firstCall = FieldNamesExtractor.getFields(TestClass.class);
        Set<String> secondCall = FieldNamesExtractor.getFields(TestClass.class);

        assertSame(firstCall, secondCall);
    }
}
