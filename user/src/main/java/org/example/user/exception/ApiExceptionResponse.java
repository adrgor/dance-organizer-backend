package org.example.user.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiExceptionResponse(String message,
                                   Throwable throwable,
                                   HttpStatus httpStatus,
                                   ZonedDateTime dateTime) {}