package com.web.controller;

import com.web.model.Trip;
import com.web.service.FavoriteTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorite-trips")
public class FavoriteTripController {
    private final FavoriteTripService favoriteTripService;

    @Autowired
    public FavoriteTripController(FavoriteTripService favoriteTripService) {
        this.favoriteTripService = favoriteTripService;
    }

    @Operation(summary = "Получить все избранные поездки")
    @GetMapping("/get")
    public List<Trip> getFavouriteTrips(){
        return favoriteTripService.getFavouriteTrips();
    }

    @Operation(summary = "Добавить поездку в избранное")
    @PutMapping("/save")
    public ResponseEntity<String> saveToFavourite(
            @Parameter(description = "ID поездки") @RequestParam(value = "tripId") Long id) {
        return favoriteTripService.saveToFavourite(id);
    }

    @Operation(summary = "Удалить поездку из избранного")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFromFavourite(@Parameter(description = "ID поездки") @RequestParam(value = "tripId") Long id){
        return favoriteTripService.deleteFromFavourite(id);
    }

    @Operation(summary = "Поменять позицию поезкди в списке избранного")
    @PutMapping("/drag-and-drop")
    public ResponseEntity<String> dragAndDrop(@Parameter(description = "ID поездки") @RequestParam(value = "tripId") Long tripId,
                                              @Parameter(description = "Новая позиция") @RequestParam(value = "newPosition") Long newPosition) {
        return favoriteTripService.dragAndDrop(tripId, newPosition);
    }
}
