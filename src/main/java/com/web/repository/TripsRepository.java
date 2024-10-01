package com.web.repository;

import com.web.model.Trip;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TripsRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t FROM Trip t WHERE " +
            " (pickupDatetime BETWEEN :startDateTime AND :endDateTime) AND " +
            " (weather.averageWindSpeed BETWEEN :minWindSpeed AND :maxWindSpeed)")
    List<Trip> filter(@Param("startDateTime") Timestamp startDateTime,
                      @Param("endDateTime") Timestamp endDateTime,
                      @Param("minWindSpeed") Double minWindSpeed,
                      @Param("maxWindSpeed") Double maxWindSpeed,
                      Pageable pageable);
}
