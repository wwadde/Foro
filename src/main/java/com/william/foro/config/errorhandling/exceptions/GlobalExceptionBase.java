package com.william.foro.config.errorhandling.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalExceptionBase extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public GlobalExceptionBase(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
