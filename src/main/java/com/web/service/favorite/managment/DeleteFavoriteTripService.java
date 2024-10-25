package com.web.service.favorite.managment;

import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteFavoriteTripService {
    private FavoriteTripRepository favoriteTripRepository;

    @Transactional
    public void execute(Long tripId) {
        Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findByTripId(tripId);

        if (favoriteTripOptional.isEmpty()) {
            throw new TripNotFoundException("Trip not found");
        }

        FavoriteTrip favoriteTrip = favoriteTripOptional.get();

        favoriteTripRepository.delete(favoriteTrip);
    }
}