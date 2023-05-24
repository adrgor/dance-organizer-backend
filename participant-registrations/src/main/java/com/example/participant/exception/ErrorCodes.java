package com.example.participant.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodes {
    public static final int INVALID_EVENT_OWNER_CODE = 3000;
    public static final int PARTICIPANT_NOT_FOUND_CODE = 4000;
    public static final int EMAIL_FIELD_NOT_FOUND_CODE = 4001;
}