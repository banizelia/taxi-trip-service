package com.banizelia.taxi.config;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Value
@Validated
@ConfigurationProperties(prefix = "favorite-trip-list")
public class FavoriteTripListConfig {
    @Min(1)
    long positionGap;

    @Min(1)
    long initialPosition;

    @Min(1)
    long minGap;

    @DecimalMin("0.1")
    @DecimalMax("0.9")
    double rebalanceThreshold;

    @Min(1)
    int batchSize;
}