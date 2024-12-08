package com.banizelia.taxi.util.extractors;

import com.banizelia.taxi.trip.model.TripDto;
import org.junit.jupiter.api.Test;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FieldAndFunctionExtractorTest {

    @Test
    void testFieldExtractorStoresNameAndExtractor() {
        Function<TripDto, Object> extractor = TripDto::getId;
        FieldAndFunctionExtractor fieldAndFunctionExtractor = new FieldAndFunctionExtractor("id", extractor);

        assertEquals("id", fieldAndFunctionExtractor.name());
        assertEquals(extractor, fieldAndFunctionExtractor.extractor());
    }

    @Test
    void testFieldExtractorAppliesExtractorFunction() {
        Function<TripDto, Object> extractor = TripDto::getId;
        FieldAndFunctionExtractor fieldAndFunctionExtractor = new FieldAndFunctionExtractor("id", extractor);

        TripDto tripDto = new TripDto();
        tripDto.getId();

        Object result = fieldAndFunctionExtractor.extractor().apply(tripDto);

        assertNull(result);
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
