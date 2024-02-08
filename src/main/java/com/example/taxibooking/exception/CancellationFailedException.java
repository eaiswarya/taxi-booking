package com.example.taxibooking.exception;

public class CancellationFailedException extends RuntimeException {
    public CancellationFailedException(String message) {
        super(message);
    }
}
