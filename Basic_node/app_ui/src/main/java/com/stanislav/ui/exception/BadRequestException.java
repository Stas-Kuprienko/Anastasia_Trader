package com.stanislav.ui.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
