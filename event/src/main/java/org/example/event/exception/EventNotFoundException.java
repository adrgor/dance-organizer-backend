package org.example.event.exception;

import lombok.Getter;

@Getter
public class EventNotFoundException extends Exception {
    private final int errorCode = ErrorCodes.EVENT_NOT_FOUND_CODE;
    private final String errorMessage = ErrorMessages.EVENT_NOT_FOUND;
}
