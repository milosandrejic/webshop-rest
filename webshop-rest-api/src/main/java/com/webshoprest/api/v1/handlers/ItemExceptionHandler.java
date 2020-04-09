package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.EmptyItemsListException;
import com.webshoprest.api.v1.exceptions.ItemLevelNotFoundException;
import com.webshoprest.api.v1.exceptions.ItemNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ItemExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ItemLevelNotFoundException.class)
    public ResponseEntity<Error> itemLevelNotFoundHandler(Exception e, WebRequest webRequest){
        Error error = new Error();
        error.setMessage("Item Level not found.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Error> itemNotFoundHandler(Exception e, WebRequest webRequest){
        Error error = new Error();
        error.setMessage("Item not found.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyItemsListException.class)
    public ResponseEntity<Error> emptyItemsListException(Exception e, WebRequest webRequest){
        Error error = new Error();
        error.setMessage("List don't have any items.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.OK);
    }

}
