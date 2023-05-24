package com.example.participant.exception;

public class ParticipantNotFoundException extends Exception {
    private final int errorCode = ErrorCodes.PARTICIPANT_NOT_FOUND_CODE;
    private final String errorMessage = ErrorMessages.PARTICIPANT_NOT_FUND;
}
