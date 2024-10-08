package com.web.repository;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {

    /**
     * Получает список поездок, отмеченных как избранные, с сортировкой по позиции.
     */
    @Query("SELECT t FROM Trip t JOIN FavoriteTrip ft ON t.id = ft.tripId ORDER BY ft.position")
    List<Trip> getFavouriteTrips();

    /**
     * Находит максимальную позицию среди избранных поездок.
     */
    @Query("SELECT COALESCE(MAX(ft.position), 0) FROM FavoriteTrip ft")
    Long findMaxPosition();

    /**
     * Инкрементирует позиции избранных поездок, которые находятся в диапазоне новых и старых позиций.
     */
    @Modifying
    @Transactional
    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position + 1 WHERE ft.position >= :newPosition AND ft.position < :oldPosition")
    void incrementPositions(@Param("newPosition") Long newPosition,
                            @Param("oldPosition") Long oldPosition);

    /**
     * Декрементирует позиции избранных поездок, которые находятся в диапазоне старых и новых позиций.
     */
    @Modifying
    @Transactional
    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position - 1 WHERE ft.position > :oldPosition AND ft.position <= :newPosition")
    void decrementPositions(@Param("oldPosition") Long oldPosition,
                            @Param("newPosition") Long newPosition);

    /**
     * Декрементирует позиции избранных поездок, которые находятся после старой позиции.
     */
    @Modifying
    @Transactional
    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position - 1 WHERE ft.position > :oldPosition")
    void decrementPositionsAfter(@Param("oldPosition") Long oldPosition);

    /**
     * Находит избранную поездку по идентификатору поездки.
     */
    Optional<FavoriteTrip> findByTripId(Long id);
}
