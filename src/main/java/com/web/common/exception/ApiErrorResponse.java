package com.web.common.exception;

public record ApiErrorResponse(int status, String error, String message) {
}