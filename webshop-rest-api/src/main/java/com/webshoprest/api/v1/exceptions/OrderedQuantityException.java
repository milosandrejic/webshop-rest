package com.webshoprest.api.v1.exceptions;

public class OrderedQuantityException extends RuntimeException {

    public OrderedQuantityException() {
        super();
    }

    public OrderedQuantityException(String message) {
        super(message);
    }

    public OrderedQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderedQuantityException(Throwable cause) {
        super(cause);
    }
}
