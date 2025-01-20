package com.banizelia.taxi.trip.export.provider;

import com.banizelia.taxi.trip.mapper.TripMapper;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.repository.TripsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TripDataProvider {
    private final TripsRepository tripsRepository;

    public Stream<TripDto> provide(TripFilterParams params) {
        return tripsRepository.streamFilter(
                params.getIsFavorite(),
                params.getPickupDateTimeFrom(),
                params.getPickupDateTimeTo(),
                params.getMinWindSpeed(),
                params.getMaxWindSpeed()
        ).map(TripMapper.INSTANCE::tripToTripDto);
    }
}