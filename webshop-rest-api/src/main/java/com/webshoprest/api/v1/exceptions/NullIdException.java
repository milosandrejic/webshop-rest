package com.webshoprest.api.v1.exceptions;

public class NullIdException extends RuntimeException {

    public NullIdException() {
        super();
    }

    public NullIdException(String message) {
        super(message);
    }

    public NullIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullIdException(Throwable cause) {
        super(cause);
    }
}
