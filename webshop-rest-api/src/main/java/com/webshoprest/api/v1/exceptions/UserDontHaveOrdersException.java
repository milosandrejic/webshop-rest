package com.webshoprest.api.v1.exceptions;

public class UserDontHaveOrdersException extends RuntimeException {

    public UserDontHaveOrdersException() {
        super();
    }

    public UserDontHaveOrdersException(String message) {
        super(message);
    }

    public UserDontHaveOrdersException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDontHaveOrdersException(Throwable cause) {
        super(cause);
    }
}
