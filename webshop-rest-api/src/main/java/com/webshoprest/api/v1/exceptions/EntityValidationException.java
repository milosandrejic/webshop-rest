package com.webshoprest.api.v1.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class EntityValidationException extends RuntimeException {

    private Map< String, String> errors;

    public EntityValidationException(Map<String, String> errors) {
        this.errors = errors;
    }



}
