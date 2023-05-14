package com.example.participant.exception;

import lombok.Getter;

@Getter
public class InvalidEventOwnerException extends Exception {
    private final int errorCode = ErrorCodes.INVALID_EVENT_OWNER_CODE;
    private final String errorMessage = ErrorMessages.INVALID_EVENT_OWNER;
}