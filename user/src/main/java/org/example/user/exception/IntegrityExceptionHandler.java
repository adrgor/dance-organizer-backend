package org.example.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class IntegrityExceptionHandler {

    @ExceptionHandler(value = {IntegrityException.class})
    public ResponseEntity<ApiExceptionResponse> handleIntegrityException(IntegrityException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        return new ResponseEntity<>(new ApiExceptionResponse(
                ex.getMessage(),
                ex,
                conflict,
                ZonedDateTime.now()
        ),conflict);
    }
}
