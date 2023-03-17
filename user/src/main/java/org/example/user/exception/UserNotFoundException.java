package org.example.user.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND);
    }

}
