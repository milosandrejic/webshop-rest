package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.ItemAlreadyAddToShoppingCartException;
import com.webshoprest.api.v1.exceptions.ShoppingCartDontBelongToUserException;
import com.webshoprest.api.v1.exceptions.ShoppingCartNotInitializedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ShoppingCartExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ShoppingCartDontBelongToUserException.class)
    public ResponseEntity<Error> shoppingCartDontBelongToUserExceptionHandler(Exception e, WebRequest webRequest) {
        Error error = new Error();
        error.setMessage("Shopping cart don't belong to user.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShoppingCartNotInitializedException.class)
    public ResponseEntity<Error> shoppingCartNotInitializedExceptionHandler(Exception e, WebRequest webRequest) {
        Error error = new Error();
        error.setMessage("Shopping cart not initialized.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemAlreadyAddToShoppingCartException.class)
    public ResponseEntity<Error> itemAlreadyAddedToCartExceptionHandler(Exception e, WebRequest webRequest){
        Error error = new Error();
        error.setMessage("Item is already added to shopping cart.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
