package com.web.controller;

import com.web.model.Trip;
import com.web.service.FavoriteTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorite-trips")
public class FavoriteTripController {
    private final FavoriteTripService favoriteTripService;

    public FavoriteTripController(FavoriteTripService favoriteTripService) {
        this.favoriteTripService = favoriteTripService;
    }

    /**
     * Получить все избранные поездки.
     *
     * @return список избранных поездок
     */
    @Operation(summary = "Получить все избранные поездки", description = "Возвращает список всех поездок, добавленных в избранное.")
    @GetMapping("/get")
    public List<Trip> getFavouriteTrips() {
        return favoriteTripService.getFavouriteTrips();
    }

    /**
     * Добавить поездку в избранное.
     *
     * @param id идентификатор поездки
     * @return статус операции добавления поездки в избранное
     */
    @Operation(summary = "Добавить поездку в избранное", description = "Добавляет поездку с указанным идентификатором в избранное.")
    @PutMapping("/save")
    public ResponseEntity<String> saveToFavourite(
            @Parameter(description = "ID поездки") @RequestParam(value = "tripId") Long id) {
        return favoriteTripService.saveToFavourite(id);
    }

    /**
     * Удалить поездку из избранного.
     *
     * @param id идентификатор поездки
     * @return статус операции удаления поездки из избранного
     */
    @Operation(summary = "Удалить поездку из избранного", description = "Удаляет поездку с указанным идентификатором из избранного.")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFromFavourite(
            @Parameter(description = "ID поездки") @RequestParam(value = "tripId") Long id) {
        return favoriteTripService.deleteFromFavourite(id);
    }

    /**
     * Поменять позицию поездки в списке избранного.
     *
     * @param tripId идентификатор поездки
     * @param newPosition новая позиция поездки в списке
     * @return статус операции перемещения поездки
     */
    @Operation(summary = "Поменять позицию поездки в списке избранного", description = "Позволяет переместить поездку в списке избранного на новую позицию.")
    @PutMapping("/drag-and-drop")
    public ResponseEntity<String> dragAndDrop(
            @Parameter(description = "ID поездки") @RequestParam(value = "tripId") Long tripId,
            @Parameter(description = "Новая позиция") @RequestParam(value = "newPosition") Long newPosition) {
        return favoriteTripService.dragAndDrop(tripId, newPosition);
    }
}
