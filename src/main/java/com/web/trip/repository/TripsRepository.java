package com.web.trip.repository;

import com.web.trip.model.Trip;
import org.springframework.data.annotation.ReadOnlyProperty;
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
    @Query("SELECT t FROM Trip t WHERE " +
            " (t.pickupDatetime BETWEEN :startDateTime AND :endDateTime) AND " +
            " (t.weather.averageWindSpeed BETWEEN :minWindSpeed AND :maxWindSpeed)")
    Page<Trip> filter(@Param("startDateTime") LocalDateTime startDateTime,
                      @Param("endDateTime") LocalDateTime endDateTime,
                      @Param("minWindSpeed") Double minWindSpeed,
                      @Param("maxWindSpeed") Double maxWindSpeed,
                      Pageable pageable);

    @ReadOnlyProperty
    @Query("select t from Trip t")
    Stream<Trip> findAllStream();
}
