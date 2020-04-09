package com.webshoprest.api.v1.validators;


import com.webshoprest.api.v1.util.ValidationErrorInspector;
import com.webshoprest.domain.Address;
import com.webshoprest.domain.ShoppingCartItem;
import com.webshoprest.repositories.ShoppingCartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

@Component
public class ShoppingCartItemValidator implements Validator {

    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    public ShoppingCartItemValidator(ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Address.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ShoppingCartItem shoppingCartItem = (ShoppingCartItem) o;
        Map<String, String> errorsMap = new HashMap<>();

        if(shoppingCartItem.getShoppingCartItemId() != null && shoppingCartItemRepository.existsById(shoppingCartItem.getShoppingCartItemId())){
            errors.rejectValue("shoppingCartItemId", "", "Item is already added to shopping cart.");
        }

        ValidationErrorInspector.inspect(errors);

    }
}
