package com.example.demo.student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;


@RestControllerAdvice
public class ApiRequestExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(ApiRequestException e) {
        // 1. create payload containing exception details
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        // 2. return response entity
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiExceptionBody);
    }
}
