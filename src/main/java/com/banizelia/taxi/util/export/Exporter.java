package com.banizelia.taxi.util.export;

import com.banizelia.taxi.trip.model.TripFilterParams;

import java.io.IOException;
import java.io.OutputStream;

public interface Exporter {
    void export(OutputStream outputStream, TripFilterParams filterParams) throws IOException;
}
