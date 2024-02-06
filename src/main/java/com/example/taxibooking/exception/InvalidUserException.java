package com.example.taxibooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserException extends RuntimeException {
    private String entity;
    private Long id;

    public InvalidUserException(String entity) {
        super("Invalid " + entity);
    }
}
