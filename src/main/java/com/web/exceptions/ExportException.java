package com.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExportException extends RuntimeException {
    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}