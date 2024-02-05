package com.example.taxibooking.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String entity;
    private final Long id;

    public EntityNotFoundException(String entity) {
        super("No entity " + entity + " found ");
        this.entity = entity;
        this.id = 0L;
    }

    public EntityNotFoundException(String entity, Long id) {
        super("User with id " + id + " not found");
        this.entity = entity;
        this.id = id;
    }
}