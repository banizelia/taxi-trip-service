package com.banizelia.taxi.util.export;

import com.banizelia.taxi.trip.mapper.TripMapper;
import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.repository.TripsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TripDataProvider {
    private final TripsRepository tripsRepository;

    public Iterator<TripDto> provide(TripFilterParams params) {
        Stream<Trip> stream = tripsRepository.streamFilter(
                params.getIsFavorite(),
                params.getPickupDateTimeFrom(),
                params.getPickupDateTimeTo(),
                params.getMinWindSpeed(),
                params.getMaxWindSpeed()
        );

        return stream.map(TripMapper.INSTANCE::tripToTripDto).iterator();
    }
}