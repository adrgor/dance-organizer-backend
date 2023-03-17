package org.example.user.exception;

public class IncorrectPasswordException extends Exception {

    public IncorrectPasswordException() {
        super(ErrorMessages.INCORRECT_PASSWORD);
    }
}
