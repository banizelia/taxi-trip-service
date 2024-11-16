package com.web.common;

public enum FavoriteTripConf {
    POSITION_GAP(10_000_000L),
    INITIAL_POSITION(10_000_000L),
    MIN_GAP(100L);

    private final long value;

    FavoriteTripConf(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
