package com.web.model.enums;

public enum FavoriteTripEnum {
    // Если список достигнет % от MAX_VALUE, произойдет ребалансировка
    REBALANCE_THRESHOLD_PERCENT(80L),

    POSITION_GAP(10_000_000L),
    INITIAL_POSITION(10_000_000L),

    MIN_GAP(100L);

    private final long value;

    FavoriteTripEnum(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
