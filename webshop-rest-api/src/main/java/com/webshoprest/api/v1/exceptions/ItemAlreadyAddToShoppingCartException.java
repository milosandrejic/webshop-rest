package com.webshoprest.api.v1.exceptions;

public class ItemAlreadyAddToShoppingCartException extends RuntimeException {
    public ItemAlreadyAddToShoppingCartException() {
        super();
    }

    public ItemAlreadyAddToShoppingCartException(String message) {
        super(message);
    }

    public ItemAlreadyAddToShoppingCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemAlreadyAddToShoppingCartException(Throwable cause) {
        super(cause);
    }
}
