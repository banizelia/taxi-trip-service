package com.banizelia.taxi.util;

public record ApiErrorResponse(int status, String error, String message) {
}