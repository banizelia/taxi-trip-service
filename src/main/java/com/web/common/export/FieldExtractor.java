package com.web.common.export;

import com.web.trip.model.TripDto;
import java.util.function.Function;

record FieldExtractor(String name, Function<TripDto, Object> extractor) {
}
