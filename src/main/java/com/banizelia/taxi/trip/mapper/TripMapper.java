package com.banizelia.taxi.trip.mapper;

import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.model.TripDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);

    @Mapping(target = "isFavorite", expression = "java(trip.getFavoriteTrip() != null)")
    @Mapping(target = "averageWindSpeed", source = "weather.averageWindSpeed")
    TripDto tripToTripDto(Trip trip);
}