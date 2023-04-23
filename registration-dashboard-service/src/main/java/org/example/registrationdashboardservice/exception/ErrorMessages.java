package org.example.registrationdashboardservice.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    public static final String INVALID_EVENT_OWNER = "Specified event owner does not match with actual owner";
}