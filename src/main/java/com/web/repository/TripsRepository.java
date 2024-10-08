package com.web.repository;

import com.web.model.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripsRepository extends JpaRepository<Trip, Long> {

    /**
     * Фильтрует поездки по дате и времени начала/окончания, а также по диапазону скорости ветра.
     * Результаты сортируются и ограничиваются с использованием Pageable.
     *
     * @param startDateTime  начальная дата и время поездки
     * @param endDateTime    конечная дата и время поездки
     * @param minWindSpeed   минимальная скорость ветра
     * @param maxWindSpeed   максимальная скорость ветра
     * @param pageable       параметры пагинации и сортировки
     * @return список отфильтрованных поездок
     */
    @Query("SELECT t FROM Trip t WHERE " +
            " (t.pickupDatetime BETWEEN :startDateTime AND :endDateTime) AND " +
            " (t.weather.averageWindSpeed BETWEEN :minWindSpeed AND :maxWindSpeed)")
    List<Trip> filter(@Param("startDateTime") LocalDateTime startDateTime,
                      @Param("endDateTime") LocalDateTime endDateTime,
                      @Param("minWindSpeed") Double minWindSpeed,
                      @Param("maxWindSpeed") Double maxWindSpeed,
                      Pageable pageable);
}
