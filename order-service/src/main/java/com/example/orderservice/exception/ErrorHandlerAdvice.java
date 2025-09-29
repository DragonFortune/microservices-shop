package com.example.orderservice.exception;

import com.example.orderservice.exception.types.BadRequestExaption;
import com.example.orderservice.exception.types.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j(topic = "ErrorHandlerAdvice")
public class ErrorHandlerAdvice {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        log.error("404 {}", exception.getMessage());
        return ErrorResponse.builder()
                .errorCode(NOT_FOUND.value())
                .errorMessage(exception.getMessage())
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestExaption.class)
    public ErrorResponse handleBadRequestExcaption(BadRequestExaption exaption) {
        log.error("400 {}", exaption.getMessage());
        return ErrorResponse.builder()
                .errorCode(BAD_REQUEST.value())
                .errorMessage(exaption.getMessage())
                .build();
    }
}
