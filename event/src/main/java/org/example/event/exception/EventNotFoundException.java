package org.example.event.exception;

import lombok.Getter;

@Getter
public class EventNotFoundException extends Exception {
    private static final int ERROR_CODE = ErrorCodes.EVENT_NOT_FOUND_CODE;
    private static final String ERROR_MESSAGE = ErrorMessages.EVENT_NOT_FOUND;
}
