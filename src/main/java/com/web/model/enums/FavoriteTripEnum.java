package com.web.model.enums;

public enum FavoriteTripEnum {
    REBALANCE_THRESHOLD_PERCENT(80L), // in %
    POSITION_GAP(1_000L),
    MIN_GAP(100L),
    INITIAL_POSITION(1_000L);

    private final long value;

    FavoriteTripEnum(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
