package com.web.favorite.repository;

import com.web.favorite.model.FavoriteTrip;
import com.web.trip.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {
    @Query("SELECT t FROM Trip t JOIN FavoriteTrip ft ON t.id = ft.tripId")
    Page<Trip> findAllWithPagination(Pageable pageable);

    @Deprecated(forRemoval = true)
    @Query("SELECT t FROM Trip t JOIN FavoriteTrip ft ON t.id = ft.tripId ORDER BY ft.position")
    List<Trip> getTripsByPositionAsc();

    @Query("SELECT ft FROM FavoriteTrip ft ORDER BY ft.position ASC")
    List<FavoriteTrip> getFavouriteTripsByPositionAsc();

    @Query("SELECT ft FROM FavoriteTrip ft WHERE ft.position BETWEEN :startPosition AND :endPosition ORDER BY ft.position ASC")
    List<FavoriteTrip> findAllByPositionBetweenOrderByPositionAsc(
            @Param("startPosition") long startPosition,
            @Param("endPosition") long endPosition
    );

    @Query("SELECT COALESCE(MAX(ft.position), 0) FROM FavoriteTrip ft")
    Optional<Long> findMaxPosition();

    @Query("SELECT MIN(ft.position) FROM FavoriteTrip ft")
    Optional<Long> findMinPosition();

    @Query("SELECT ft.position FROM FavoriteTrip ft ORDER BY ft.position ASC OFFSET :index ROWS FETCH FIRST 1 ROWS ONLY")
    Optional<Long> findPositionByIndex(@Param("index") Long index);

    Optional<FavoriteTrip> findByTripId(Long id);
}
