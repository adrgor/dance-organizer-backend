package org.example.user.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodes {
    public static final int EMAIL_TAKEN_CODE = 1000;
    public static final int USERNAME_TAKEN_CODE = 1001;
    public static final int USER_NOT_ACTIVATED_CODE = 1002;
    public static final int INCORRECT_PASSWORD_CODE = 1003;
    public static final int USER_NOT_FOUND_CODE = 1004;
}
