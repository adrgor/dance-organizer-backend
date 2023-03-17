package org.example.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = {IntegrityException.class})
    public ResponseEntity<ApiExceptionResponse> handleIntegrityException(IntegrityException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        return new ResponseEntity<>(new ApiExceptionResponse(
                ex.getMessage(),
                ex,
                ex.getErrorCode(),
                ZonedDateTime.now()
        ), conflict);
    }

    @ExceptionHandler(value = {UserNotActivatedException.class})
    public ResponseEntity<ApiExceptionResponse> handleUserNotActivatedException(UserNotActivatedException ex) {

        return new ResponseEntity<>(new ApiExceptionResponse(
                ex.getMessage(),
                ex,
                ErrorCodes.USER_NOT_ACTIVATED_CODE,
                ZonedDateTime.now()
        ), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {IncorrectPasswordException.class})
    public ResponseEntity<ApiExceptionResponse> handleIncorrectPasswordException(IncorrectPasswordException ex) {

        return new ResponseEntity<>(new ApiExceptionResponse(
                ex.getMessage(),
                ex,
                ErrorCodes.INCORRECT_PASSWORD_CODE,
                ZonedDateTime.now()
        ), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ApiExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {

        return new ResponseEntity<>(new ApiExceptionResponse(
                ex.getMessage(),
                ex,
                ErrorCodes.USER_NOT_FOUND_CODE,
                ZonedDateTime.now()
        ), HttpStatus.UNAUTHORIZED);
    }
}
