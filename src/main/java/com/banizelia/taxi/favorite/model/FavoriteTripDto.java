package com.banizelia.taxi.favorite.model;

import lombok.Data;

@Data
public class FavoriteTripDto {
    private Long id;
    private Long tripId;
    private Long position;
}