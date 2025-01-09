package com.banizelia.taxi.util.export;

import com.banizelia.taxi.trip.export.provider.TripDataProvider;
import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.repository.TripsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.stream.Stream;

class TripDataProviderTest {
    @Test
    void testProvide() {
        TripsRepository repo = Mockito.mock(TripsRepository.class);
        Mockito.when(repo.streamFilter(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Stream.of(new Trip()));
        TripDataProvider provider = new TripDataProvider(repo);
        Assertions.assertEquals(1, provider.provide(new TripFilterParams()).count());
    }
}
