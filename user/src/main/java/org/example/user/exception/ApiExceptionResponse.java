package org.example.user.exception;

import java.time.ZonedDateTime;

public record ApiExceptionResponse(String message,
                                   Throwable throwable,
                                   int errorCode,
                                   ZonedDateTime dateTime) {}