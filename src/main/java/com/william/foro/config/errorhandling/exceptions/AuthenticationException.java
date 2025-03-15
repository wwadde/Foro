package com.william.foro.config.errorhandling.exceptions;

import org.springframework.http.HttpStatus;


public class AuthenticationException extends GlobalExceptionBase {
    public AuthenticationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
