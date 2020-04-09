package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.OrderAlreadyCreatedException;
import com.webshoprest.api.v1.exceptions.OrderNotFoundException;
import com.webshoprest.api.v1.exceptions.OrderedQuantityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Error> orderNotFoundException(Exception e, WebRequest webRequest) {
        Error error = new Error();
        error.setMessage("Order not found.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderedQuantityException.class)
    public ResponseEntity<Error> orderedQuantityExceptionHandler(Exception e, WebRequest webRequest) {
        Error error = new Error();
        error.setMessage("Ordered quantity is bigger then our quantity.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderAlreadyCreatedException.class)
    public ResponseEntity<Error> orderAlreadyCreatedExceptionHandler(Exception e, WebRequest webRequest) {
        Error error = new Error();
        error.setMessage("Same order already created.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
