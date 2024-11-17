package com.web.favorite.service;

import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteFavoriteTripService {
    private FavoriteTripRepository favoriteTripRepository;

    @Transactional
    public void execute(Long tripId) {
        FavoriteTrip favoriteTrip = favoriteTripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));

        favoriteTripRepository.delete(favoriteTrip);
    }
}