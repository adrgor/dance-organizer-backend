package com.example.participant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class RegistrationDashboardExceptionHandler {

    @ExceptionHandler(value = {InvalidEventOwnerException.class})
    public ResponseEntity<ApiExceptionResponse> handleIntegrityException(InvalidEventOwnerException ex) {
        return new ResponseEntity<>(new ApiExceptionResponse(
                ex.getMessage(),
                ex,
                ex.getErrorCode(),
                ZonedDateTime.now()
        ), HttpStatus.UNAUTHORIZED);
    }
}