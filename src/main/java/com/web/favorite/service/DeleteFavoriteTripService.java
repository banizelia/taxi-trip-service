package com.web.favorite.service;

import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteFavoriteTripService {
    private final FavoriteTripRepository favoriteTripRepository;

    @Transactional
    public void execute(Long tripId) {
        FavoriteTrip favoriteTrip = favoriteTripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));

        try {
            favoriteTripRepository.delete(favoriteTrip);
        } catch (OptimisticLockException e) {
            log.warn("Optimistic lock exception while deleting favoriteTrip: id={}, tripId={}, position={}",
                    favoriteTrip.getId(),
                    favoriteTrip.getTripId(),
                    favoriteTrip.getPosition(),
                    e
            );
            throw e;
        }
    }
}