package com.example.orderservice.exception.types;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
