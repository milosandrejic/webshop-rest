package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.UserDontHaveOrdersException;
import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserDontHaveOrdersException.class)
    public ResponseEntity<Error> userDontHaveOrdersExceptionHandler(Exception e, WebRequest webRequest) {
        Error error = new Error();
        error.setMessage("User don't have any orders.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> userNotFoundExceptionHandler(Exception e, WebRequest webRequest) {
        Error error = new Error();
        error.setMessage("User not found.");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
