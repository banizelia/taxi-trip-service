package com.web.service.favorite.managment;

import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.mapper.TripMapper;
import com.web.repository.FavoriteTripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetFavoriteTripService {
    private FavoriteTripRepository favoriteTripRepository;

    public List<TripDto> execute() {
        List<Trip> favoriteTrips = favoriteTripRepository.getFavouriteTrips();
        return favoriteTrips.stream()
                .map(TripMapper.INSTANCE::tripToTripDto)
                .toList();
    }
}
