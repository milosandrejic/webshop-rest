package com.webshoprest.api.v1.validators;

import com.webshoprest.api.v1.util.ValidationErrorInspector;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return LoginRequestValidator.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationErrorInspector.inspect(errors);
    }
}
