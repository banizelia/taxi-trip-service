package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
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
            @Parameter(description = "Начальная дата и время поездки") @RequestParam(value = "startDateTime", required = false, defaultValue = "2016-01-01 00:00:00") Timestamp startDateTime,
            @Parameter(description = "Конечная дата и время поездки") @RequestParam(value = "endDateTime", required = false, defaultValue = "2016-01-31 23:59:59") Timestamp endDateTime,
            @Parameter(description = "Минимальная скорость ветра") @RequestParam(value = "minWindSpeed", required = false, defaultValue = "0") Double minWindSpeed,
            @Parameter(description = "Максимальная скорость ветра") @RequestParam(value = "maxWindSpeed", required = false, defaultValue = "9999") Double maxWindSpeed,
            @Parameter(description = "Направление сортировки (asc/desc)") @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction, /* asc or desc */
            @Parameter(description = "Поле, по которому будет происходить сортировка") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy, /* pickupDatetime or averageWindSpeed */
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Размер страницы") @RequestParam(value = "pageSize", defaultValue = "500") Integer pageSize ){

        return tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
    }

    @Operation(summary = "Экспорт поездок в Excel")
    @GetMapping("/download")
    public ResponseEntity<Resource> download() {
        return tripService.download();
    }
}