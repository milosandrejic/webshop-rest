package com.webshoprest.api.v1.util;

public enum EmailType {

    CONFIRMATION("confirm email."),
    RESET_PASSWORD("reset your password");

    private String text;

    EmailType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
