package com.web.common.export;

import com.web.trip.model.TripDto;
import org.junit.jupiter.api.Test;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FieldExtractorTest {

    @Test
    void testFieldExtractorStoresNameAndExtractor() {
        Function<TripDto, Object> extractor = TripDto::getId; // Пример функции
        FieldExtractor fieldExtractor = new FieldExtractor("id", extractor);

        assertEquals("id", fieldExtractor.name());
        assertEquals(extractor, fieldExtractor.extractor());
    }

    @Test
    void testFieldExtractorAppliesExtractorFunction() {
        Function<TripDto, Object> extractor = TripDto::getId;
        FieldExtractor fieldExtractor = new FieldExtractor("id", extractor);

        TripDto tripDto = new TripDto();
        tripDto.getId(); // Установи значение для теста

        Object result = fieldExtractor.extractor().apply(tripDto);

        assertEquals(null, result);
    }

    @Test
    void testFieldExtractorWithNullValue() {
        Function<TripDto, Object> extractor = TripDto::getId;
        FieldExtractor fieldExtractor = new FieldExtractor("id", extractor);

        TripDto tripDto = new TripDto(); // Поле destination оставлено null

        Object result = fieldExtractor.extractor().apply(tripDto);

        assertNull(result);
    }
}
