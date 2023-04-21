package org.example.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class EventExceptionHandler {

    @ExceptionHandler(value = {EventNotFoundException.class})
    public ResponseEntity<ApiExceptionResponse> handleIntegrityException(EventNotFoundException ex) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ApiExceptionResponse(
                ex.getMessage(),
                ex,
                ex.getErrorCode(),
                ZonedDateTime.now()
        ), notFound);
    }
}
