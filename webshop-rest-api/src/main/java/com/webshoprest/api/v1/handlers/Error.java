package com.webshoprest.api.v1.handlers;

import java.time.LocalDate;

public class Error {

    private final LocalDate date = LocalDate.now();
    private String message;

    public Error() {
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
