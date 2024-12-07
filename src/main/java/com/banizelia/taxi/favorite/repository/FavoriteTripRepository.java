package com.banizelia.taxi.favorite.repository;

import com.banizelia.taxi.favorite.model.FavoriteTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {
    @Query("SELECT MAX(ft.position) FROM FavoriteTrip ft")
    Optional<Long> findMaxPosition();

    @Query("SELECT MIN(ft.position) FROM FavoriteTrip ft")
    Optional<Long> findMinPosition();

    @Query("SELECT ft.position FROM FavoriteTrip ft ORDER BY ft.position ASC OFFSET :index ROWS FETCH FIRST 1 ROWS ONLY")
    Optional<Long> findPositionByIndex(@Param("index") Long index);

    List<FavoriteTrip> findAllByOrderByPositionAsc();
    Optional<FavoriteTrip> findByTripId(Long id);
}
