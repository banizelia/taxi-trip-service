package com.banizelia.taxi.util.export;

import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.repository.TripsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripDataProviderTest {
    @Test
    void testProvide() {
        TripsRepository repo = Mockito.mock(TripsRepository.class);
        Trip trip = new Trip();
        trip.setId(1L);
        Mockito.when(repo.streamFilter(any(), any(), any(), any(), any()))
                .thenReturn(Stream.of(trip));
        TripDataProvider provider = new TripDataProvider(repo);
        TripFilterParams params = new TripFilterParams();
        var it = provider.provide(params);
        assertTrue(it.hasNext());
        TripDto dto = it.next();
        assertEquals(1L, (long) dto.getId());
    }
}

