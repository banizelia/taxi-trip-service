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
    @GetMapping
    public ResponseEntity<List<Trip>> filterTrips(
            @Parameter(description = "Начальная дата и время поездки", example = "2016-01-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-01-01T00:00:00.000") LocalDateTime startDateTime,

            @Parameter(description = "Конечная дата и время поездки", example = "2016-02-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-02-01T00:00:00.000") LocalDateTime endDateTime,

            @Parameter(description = "Минимальная скорость ветра")
            @RequestParam(required = false, defaultValue = "0") Double minWindSpeed,

            @Parameter(description = "Максимальная скорость ветра")
            @RequestParam(required = false, defaultValue = "9999") Double maxWindSpeed,

            @Parameter(description = "Направление сортировки (asc/desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction,

            @Parameter(description = "Поле, по которому будет происходить сортировка")
            @RequestParam(required = false, defaultValue = "id") String sortBy,

            @Parameter(description = "Номер страницы")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Размер страницы")
            @RequestParam(defaultValue = "20") Integer pageSize) {

        System.out.println(startDateTime);

        List<Trip> trips = tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
        return ResponseEntity.ok(trips);
    }

    @Operation(summary = "Экспортирует поездки в формате Excel с возможностью ограничения количества листов.")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@Parameter(description = "Ограничение по листам") @RequestParam(value = "listsLimit", defaultValue = "2") Integer listsLimit) {
        return tripService.download(listsLimit);
    }
}