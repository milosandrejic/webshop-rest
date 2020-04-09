package com.webshoprest.api.v1.exceptions;

public class ShoppingCartNotInitializedException extends RuntimeException {

    public ShoppingCartNotInitializedException() {
        super();
    }

    public ShoppingCartNotInitializedException(String message) {
        super(message);
    }

    public ShoppingCartNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShoppingCartNotInitializedException(Throwable cause) {
        super(cause);
    }
}
