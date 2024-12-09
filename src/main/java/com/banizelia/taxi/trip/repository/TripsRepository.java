package com.banizelia.taxi.trip.repository;

import com.banizelia.taxi.trip.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface TripsRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT t FROM Trip t " +
            "LEFT JOIN t.favoriteTrip ft " +
            "WHERE (t.pickupDatetime BETWEEN :startDateTime AND :endDateTime) " +
            "AND (t.weather.averageWindSpeed BETWEEN :minWindSpeed AND :maxWindSpeed) " +
            "AND (:isFavorite IS NULL OR (ft.id IS NOT NULL AND :isFavorite = TRUE) OR (ft.id IS NULL AND :isFavorite = FALSE))")
    Page<Trip> filter(
            @Param("isFavorite") Boolean isFavorite,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("minWindSpeed") Double minWindSpeed,
            @Param("maxWindSpeed") Double maxWindSpeed,
            Pageable pageable
    );


    @Query("SELECT t FROM Trip t " +
            "LEFT JOIN t.favoriteTrip ft " +
            "WHERE (t.pickupDatetime BETWEEN :startDateTime AND :endDateTime) " +
            "AND (t.weather.averageWindSpeed BETWEEN :minWindSpeed AND :maxWindSpeed) " +
            "AND (:isFavorite IS NULL OR (ft.id IS NOT NULL AND :isFavorite = TRUE) OR (ft.id IS NULL AND :isFavorite = FALSE))")
    Stream<Trip> streamFilter(
            @Param("isFavorite") Boolean isFavorite,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("minWindSpeed") Double minWindSpeed,
            @Param("maxWindSpeed") Double maxWindSpeed
    );
}