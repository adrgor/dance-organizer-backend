package org.example.registrationdashboardservice.exception;

import java.time.ZonedDateTime;

public record ApiExceptionResponse(String message, Throwable throwable, int errorCode, ZonedDateTime dateTime) {
}