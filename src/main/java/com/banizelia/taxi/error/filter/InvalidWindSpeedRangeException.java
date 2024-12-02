package com.banizelia.taxi.error.filter;

public class InvalidWindSpeedRangeException extends FilterValidationException {
    public InvalidWindSpeedRangeException(Double minWindSpeed, Double maxWindSpeed) {
        super(String.format("maxWindSpeed is smaller or equal to minWindSpeed, maxWindSpeed = %f minWindSpeed = %f",
                maxWindSpeed, minWindSpeed));
    }
}