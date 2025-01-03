package com.web.repository;

import com.web.model.Trip;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;
import static org.hibernate.jpa.QueryHints.HINT_READONLY;

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

    @Query("select t from Trip t")
    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "10000"),
            @QueryHint(name = HINT_READONLY, value = "true")
    })
    Stream<Trip> findAllStream(@Param("batchSize") int batchSize);
}
