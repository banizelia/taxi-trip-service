package com.web.repository;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {
    @Query("SELECT t FROM Trip t JOIN FavoriteTrip ft ON t.id = ft.tripId ORDER BY ft.position")
    List<Trip> getFavouriteTrips();

    @Query("SELECT COALESCE(MAX(ft.position), 0) FROM FavoriteTrip ft")
    Long findMaxPosition();

    Optional<FavoriteTrip> findByTripId(Long id);
    
    @Query("SELECT ft.position FROM FavoriteTrip ft WHERE ft.tripId = :tripId")
    Optional<Long> findPositionByIndex(@Param("tripId") Long tripId);

    List<FavoriteTrip> findAllByOrderByPositionAsc();
}
