package com.api.repository;

import com.api.model.FavoriteTrip;
import com.api.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {

    @Query("SELECT t FROM Trip t JOIN t.favoriteTrip ft")
    List<Trip> getFavouriteTrips();

}
