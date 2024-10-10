package com.web.service;

import com.web.model.Trip;
import com.web.repository.TripsRepository;
import com.web.util.ExcelHelper;
import com.web.util.FieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Сервис для работы с данными поездок.
 */
@Service
public class TripService {

    @Autowired
    TripsRepository tripsRepository;

    public ResponseEntity<List<Trip>> filter(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                             Double minWindSpeed, Double maxWindSpeed,
                                             String direction, String sortBy,
                                             Integer page, Integer pageSize) {

        // Проверка на корректность периода времени
        if (endDateTime.isBefore(startDateTime)){
            throw new IllegalArgumentException("endDateTime is before startDateTime, startDateTime = " + startDateTime + " endDateTime = " + endDateTime);
        }

        // Проверка на корректность периода времени
        if (maxWindSpeed <= minWindSpeed){
            throw new IllegalArgumentException("maxWindSpeed is smaller or equal to minWindSpeed, maxWindSpeed = " + maxWindSpeed + " minWindSpeed = " + minWindSpeed);
        }

        // Проверка на корректность номера страницы
        if (page < 0){
            throw new IllegalArgumentException("Invalid page field, smaller than zero: " + page);
        }

        // Проверка на корректность направления сортировки
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new IllegalArgumentException("Invalid direction : " + direction);
        }

        // Создаем набор разрешенных полей для сортировки (Trip и Weather)
        Set<String> weatherAndTripAllowedField = new HashSet<>();
        weatherAndTripAllowedField.addAll(FieldUtil.getTripFields());
        weatherAndTripAllowedField.addAll(FieldUtil.getWeatherFields());

        // Проверка на наличие указанного поля для сортировки
        if (!weatherAndTripAllowedField.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        // Формирование параметров сортировки и пагинации
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        // Возвращаем отфильтрованные данные
        return ResponseEntity.ok(tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable));
    }

    public ResponseEntity<Resource> download(Integer sheetLimit) {
        // Проверка на корректность ограничения по листам
        if (sheetLimit < 1){
            throw new IllegalArgumentException("listsLimit cannot be less than one");
        }

        String filename = "trips.xlsx";
        InputStreamResource file = new InputStreamResource(ExcelHelper.tripsToExcel(tripsRepository, sheetLimit));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
