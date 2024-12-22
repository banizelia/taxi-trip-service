package com.banizelia.taxi.error.position;

public class PositionOverflowException extends PositionException {
    public PositionOverflowException(long maxPosition, double thresholdPercent) {
        super(String.format(
                "Unable to calculate next position even after sparsification. Current max position: %d exceeds %f%% of Long.MAX_VALUE",
                maxPosition,
                thresholdPercent
        ));
    }
}