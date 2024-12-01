package com.web.common.export;

import com.web.trip.model.TripDto;
import org.junit.jupiter.api.Test;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FieldAndFunctionExtractorTest {

    @Test
    void testFieldExtractorStoresNameAndExtractor() {
        Function<TripDto, Object> extractor = TripDto::getId; // Пример функции
        FieldAndFunctionExtractor fieldAndFunctionExtractor = new FieldAndFunctionExtractor("id", extractor);

        assertEquals("id", fieldAndFunctionExtractor.name());
        assertEquals(extractor, fieldAndFunctionExtractor.extractor());
    }

    @Test
    void testFieldExtractorAppliesExtractorFunction() {
        Function<TripDto, Object> extractor = TripDto::getId;
        FieldAndFunctionExtractor fieldAndFunctionExtractor = new FieldAndFunctionExtractor("id", extractor);

        TripDto tripDto = new TripDto();
        tripDto.getId(); // Установи значение для теста

        Object result = fieldAndFunctionExtractor.extractor().apply(tripDto);

        assertEquals(null, result);
    }

    @Test
    void testFieldExtractorWithNullValue() {
        Function<TripDto, Object> extractor = TripDto::getId;
        FieldAndFunctionExtractor fieldAndFunctionExtractor = new FieldAndFunctionExtractor("id", extractor);

        TripDto tripDto = new TripDto();

        Object result = fieldAndFunctionExtractor.extractor().apply(tripDto);

        assertNull(result);
    }
}
