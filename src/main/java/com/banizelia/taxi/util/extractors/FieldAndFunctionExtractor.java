package com.banizelia.taxi.util.extractors;

import com.banizelia.taxi.trip.model.TripDto;

import java.util.function.Function;

public record FieldAndFunctionExtractor(String name, Function<TripDto, Object> extractor) {
}
