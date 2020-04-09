package com.webshoprest.api.v1.exceptions;

public class OrderAlreadyCreatedException extends RuntimeException {

    public OrderAlreadyCreatedException() {
        super();
    }

    public OrderAlreadyCreatedException(String message) {
        super(message);
    }

    public OrderAlreadyCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderAlreadyCreatedException(Throwable cause) {
        super(cause);
    }
}
