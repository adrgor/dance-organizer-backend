package org.example.user.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    public static final String EMAIL_TAKEN = "Specified email is already in use";
    public static final String USERNAME_TAKEN = "Specified username is already in use";
    public static final String USER_NOT_ACTIVATED = "Specified user has not been activated yet";
    public static final String INCORRECT_PASSWORD = "Specified password is invalid for given user";
    public static final String USER_NOT_FOUND = "Specified user was not found";
}
