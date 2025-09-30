package com.example.orderservice.exception;

import com.example.orderservice.exception.types.BadRequestException;
import com.example.orderservice.exception.types.ConflictException;
import com.example.orderservice.exception.types.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

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
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequestException(BadRequestException exception) {
        log.error("400 {}", exception.getMessage());
        return ErrorResponse.builder()
                .errorCode(BAD_REQUEST.value())
                .errorMessage(exception.getMessage())
                .build();
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorResponse handleConflictException(ConflictException exception) {
        log.error("409 {}", exception.getMessage());
        return ErrorResponse.builder()
                .errorCode(CONFLICT.value())
                .errorMessage(exception.getMessage())
                .build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception exception) {
        log.error("500 {}", exception.getMessage(), exception);
        return ErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR.value())
                .errorMessage("Unexpected error occurred. Please try again later.")
                .build();
    }
}
