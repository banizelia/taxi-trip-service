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

    /**
     * Фильтрация поездок по дате, скорости ветра и сортировке.
     *
     * @param startDateTime дата и время начала
     * @param endDateTime дата и время окончания
     * @param minWindSpeed минимальная скорость ветра
     * @param maxWindSpeed максимальная скорость ветра
     * @param direction направление сортировки (asc или desc)
     * @param sortBy поле для сортировки
     * @param page номер страницы
     * @param pageSize размер страницы
     * @return список отфильтрованных поездок
     */
    public List<Trip> filter(LocalDateTime startDateTime, LocalDateTime endDateTime,
                             Double minWindSpeed, Double maxWindSpeed,
                             String direction, String sortBy,
                             Integer page, Integer pageSize) {

        // Проверка на корректность периода времени
        if (endDateTime.isBefore(startDateTime)){
            throw new IllegalArgumentException("endDateTime is before startDateTime, startDateTime = " + startDateTime + " endDateTime = " + endDateTime);
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
        return tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);
    }

    /**
     * Экспорт поездок в Excel файл и отправка его как вложение.
     *
     * @param sheetLimit максимальное количество листов в Excel файле
     * @return ResponseEntity с Excel файлом
     */
    public ResponseEntity<Resource> download(Integer sheetLimit) {
        // Проверка на корректность ограничения по листам
        if (sheetLimit < 1){
            throw new IllegalArgumentException("listsLimit cannot be less than one");
        }

        String filename = "trips.xlsx";
        InputStreamResource file = new InputStreamResource(ExcelHelper.tripsToExcel(tripsRepository, sheetLimit));

        // Формирование ответа с файлом для скачивания
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
