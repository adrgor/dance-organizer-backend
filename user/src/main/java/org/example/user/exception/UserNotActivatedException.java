package org.example.user.exception;

public class UserNotActivatedException extends Exception {

    public UserNotActivatedException() {
        super(ErrorMessages.USER_NOT_ACTIVATED);
    }
}
