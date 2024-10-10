package com.web.controller;

import com.web.model.Trip;
import com.web.service.FavoriteTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite-trips")
public class FavoriteTripController {
    private final FavoriteTripService favoriteTripService;

    public FavoriteTripController(FavoriteTripService favoriteTripService) {
        this.favoriteTripService = favoriteTripService;
    }

    @Operation(summary = "Получить все избранные поездки", description = "Возвращает список всех поездок, добавленных в избранное.")
    @GetMapping()
    public ResponseEntity<List<Trip>> getFavouriteTrips() {
        return favoriteTripService.getFavouriteTrips();
    }

    @Operation(summary = "Добавить поездку в избранное", description = "Добавляет поездку с указанным идентификатором в избранное.")
    @PutMapping()
    public ResponseEntity<String> saveToFavourite(@Parameter(description = "ID поездки") @RequestParam("tripId") Long id) {
        return favoriteTripService.saveToFavourite(id);
    }

    @Operation(summary = "Удалить поездку из избранного", description = "Удаляет поездку с указанным идентификатором из избранного.")
    @DeleteMapping()
    public ResponseEntity<String> deleteFromFavourite(@Parameter(description = "ID поездки") @RequestParam("tripId") Long id) {
        return favoriteTripService.deleteFromFavourite(id);
    }

    @Operation(summary = "Поменять позицию поездки в списке избранного", description = "Позволяет переместить поездку в списке избранного на новую позицию.")
    @PutMapping("/drag-and-drop")
    public ResponseEntity<String> dragAndDrop(
            @Parameter(description = "ID поездки") @RequestParam(value = "tripId") Long tripId,
            @Parameter(description = "Новая позиция") @RequestParam(value = "newPosition") Long newPosition) {
        return favoriteTripService.dragAndDrop(tripId, newPosition);
    }

    @Operation(summary = "Проверка и исправление порядка в списке избранного")
    @PutMapping("/validate-positions")
    public ResponseEntity<String> validatePositions() {
        return favoriteTripService.validatePositions();
    }
}