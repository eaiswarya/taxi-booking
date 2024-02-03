package com.example.taxibooking.exception;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException {

    private final String entity;

    public EntityAlreadyExistsException(String entity) {
        this.entity = entity;
    }
}
