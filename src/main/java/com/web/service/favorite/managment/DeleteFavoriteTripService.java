package com.web.service.favorite.managment;

import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.repository.FavoriteTripRepository;
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
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));

        favoriteTripRepository.delete(favoriteTrip);
    }
}