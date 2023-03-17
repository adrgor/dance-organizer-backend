package org.example.user.exception;

import lombok.Getter;

@Getter
public class IntegrityException extends RuntimeException{
    private final int errorCode;

    public IntegrityException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
