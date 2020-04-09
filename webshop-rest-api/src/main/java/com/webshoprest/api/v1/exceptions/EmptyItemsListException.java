package com.webshoprest.api.v1.exceptions;

public class EmptyItemsListException extends RuntimeException {

    public EmptyItemsListException() {
        super();
    }

    public EmptyItemsListException(String message) {
        super(message);
    }

    public EmptyItemsListException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyItemsListException(Throwable cause) {
        super(cause);
    }
}
