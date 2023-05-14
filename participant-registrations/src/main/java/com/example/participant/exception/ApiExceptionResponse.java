package com.example.participant.exception;

import java.time.ZonedDateTime;

public record ApiExceptionResponse(String message, Throwable throwable, int errorCode, ZonedDateTime dateTime) {
}