package com.webshoprest.api.v1.exceptions;

public class ShoppingCartDontBelongToUserException extends RuntimeException {

    public ShoppingCartDontBelongToUserException() {
        super();
    }

    public ShoppingCartDontBelongToUserException(String message) {
        super(message);
    }

    public ShoppingCartDontBelongToUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShoppingCartDontBelongToUserException(Throwable cause) {
        super(cause);
    }
}
