package com.example.orderservice.exception.types;

public class BadRequestExaption extends RuntimeException {
    public BadRequestExaption(String message) {
        super(message);
    }
}
