package com.example.taxibooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    private final String entity;
    private final Long id;

    public EntityNotFoundException(String entity) {
        super("No entity " + entity + " found ");
        this.entity = entity;
        this.id = 0L;
    }

    public EntityNotFoundException(String entity, Long id) {
        super(entity + " with id " + id + " not found");
        this.entity = entity;
        this.id = id;
    }
}
