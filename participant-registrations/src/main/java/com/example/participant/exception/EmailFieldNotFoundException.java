package com.example.participant.exception;

public class EmailFieldNotFoundException extends Exception {
    private final int errorCode = ErrorCodes.EMAIL_FIELD_NOT_FOUND_CODE;
    private final String errorMessage = ErrorMessages.EMAIL_FIELD_NOT_FUND;
}
