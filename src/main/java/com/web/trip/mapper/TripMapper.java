package com.web.trip.mapper;

import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);

    @Mapping(target = "isFavorite", expression = "java(trip.getFavoriteTrip() != null)")
    TripDto tripToTripDto(Trip trip);
}