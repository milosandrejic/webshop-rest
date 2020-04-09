package com.webshoprest.api.v1.validators;

import com.webshoprest.api.v1.util.ValidationErrorInspector;
import com.webshoprest.domain.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Item.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationErrorInspector.inspect(errors);
    }
}
