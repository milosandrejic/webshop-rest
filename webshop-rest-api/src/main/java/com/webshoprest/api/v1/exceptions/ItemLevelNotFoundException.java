package com.webshoprest.api.v1.exceptions;

public class ItemLevelNotFoundException extends RuntimeException {

    public ItemLevelNotFoundException() {
        super();
    }

    public ItemLevelNotFoundException(String message) {
        super(message);
    }

    public ItemLevelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemLevelNotFoundException(Throwable cause) {
        super(cause);
    }
}
