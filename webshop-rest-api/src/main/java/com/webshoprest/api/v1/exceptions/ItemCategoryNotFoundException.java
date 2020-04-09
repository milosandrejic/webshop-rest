package com.webshoprest.api.v1.exceptions;

public class ItemCategoryNotFoundException extends RuntimeException {

    public ItemCategoryNotFoundException() {
        super();
    }

    public ItemCategoryNotFoundException(String message) {
        super(message);
    }

    public ItemCategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemCategoryNotFoundException(Throwable cause) {
        super(cause);
    }
}
