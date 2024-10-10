package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "Фильтрация поездок", description = "Позволяет фильтровать поездки по дате, скорости ветра, а также сортировать и разбивать на страницы.")
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

            @Parameter(description = "Направление сортировки (asc или desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction,

            @Parameter(description = "Поле, по которому будет происходить сортировка")
            @RequestParam(required = false, defaultValue = "id") String sortBy,

            @Parameter(description = "Номер страницы")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Размер страницы")
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
    }

    @Operation(summary = "Экспорт поездок в формате Excel", description = "Экспортирует список поездок в формате Excel, с возможностью задать ограничение по количеству листов.")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@Parameter(description = "Ограничение по листам") @RequestParam(value = "sheetLimit", defaultValue = "2") Integer sheetLimit) {
        return tripService.download(sheetLimit);
    }
}
