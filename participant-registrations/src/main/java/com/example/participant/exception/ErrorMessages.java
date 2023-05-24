package com.example.participant.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    public static final String INVALID_EVENT_OWNER = "Specified event owner does not match with actual owner";
    public static final String PARTICIPANT_NOT_FUND = "Participant with given id doesn't exist";
    public static final String EMAIL_FIELD_NOT_FUND = "Registration form for given event doesn't consist email field";
}