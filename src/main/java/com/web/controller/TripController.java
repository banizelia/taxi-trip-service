package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "Фильтрация поездок")
    @GetMapping("/filter")
    public List<Trip> filter(
            @Parameter(description = "Начальная дата и время поездки") @RequestParam(value = "startDateTime", required = false, defaultValue = "2016-01-01 00:00:00")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDateTime,

            @Parameter(description = "Конечная дата и время поездки") @RequestParam(value = "endDateTime", required = false, defaultValue = "2016-01-31 23:59:59")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDateTime,

            @Parameter(description = "Минимальная скорость ветра") @RequestParam(value = "minWindSpeed", required = false, defaultValue = "0") Double minWindSpeed,
            @Parameter(description = "Максимальная скорость ветра") @RequestParam(value = "maxWindSpeed", required = false, defaultValue = "9999") Double maxWindSpeed,
            @Parameter(description = "Направление сортировки (asc/desc)") @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction,
            @Parameter(description = "Поле, по которому будет происходить сортировка (pickupDatetime)") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Размер страницы") @RequestParam(value = "pageSize", defaultValue = "500") Integer pageSize){

        return tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
    }

    @Operation(summary = "Экспортирует поездки в формате Excel с возможностью ограничения количества листов.")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@Parameter(description = "Ограничение по листам") @RequestParam(value = "listsLimit", defaultValue = "2") Integer listsLimit) {
        return tripService.download(listsLimit);
    }
}