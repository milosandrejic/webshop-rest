package com.webshoprest.api.v1.exceptions;

public class EmptyImageException extends RuntimeException {

    public EmptyImageException() {
        super();
    }

    public EmptyImageException(String message) {
        super(message);
    }

    public EmptyImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyImageException(Throwable cause) {
        super(cause);
    }
}
