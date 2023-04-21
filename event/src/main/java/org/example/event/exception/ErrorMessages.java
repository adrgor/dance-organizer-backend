package org.example.event.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    public static final String EVENT_NOT_FOUND = "Specified event does not exist";
}
