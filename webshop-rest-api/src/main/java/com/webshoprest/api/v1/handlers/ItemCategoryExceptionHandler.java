package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.ItemCategoryNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ItemCategoryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemCategoryNotFoundException.class)
    public ResponseEntity<Error> itemCategoryNotFoundHandler(Exception e, WebRequest webRequest){
        Error error = new Error();
        error.setMessage("Item Category not found.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
