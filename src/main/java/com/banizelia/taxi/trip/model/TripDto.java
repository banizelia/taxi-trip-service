package com.banizelia.taxi.trip.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TripDto {
    private Boolean isFavorite;
    private Long id;
    private Integer vendorId;
    private LocalDateTime pickupDatetime;
    private LocalDateTime dropoffDatetime;
    private Integer passengerCount;
    private Double tripDistance;
    private Integer rateCodeId;
    private String storeAndFwdFlag;
    private Integer pickupLocationId;
    private Integer dropoffLocationId;
    private Integer paymentType;
    private Double fareAmount;
    private Double extra;
    private Double mtaTax;
    private Double tipAmount;
    private Double tollsAmount;
    private Double improvementSurcharge;
    private Double totalAmount;
    private Double congestionSurcharge;
    private Double airportFee;
}