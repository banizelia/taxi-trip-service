package com.web.common.config;

public enum FavoriteTripListConf {
    POSITION_GAP(10_000_000L),
    INITIAL_POSITION(10_000_000L),
    MIN_GAP(100L);

    private final long value;

    FavoriteTripListConf(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
