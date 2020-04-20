package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AccessDeniedHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Error> invalidTokenExceptionHandler() {
        Error error = new Error();
        error.setMessage("Invalid token.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Error> accessDeniedExceptionHandler() {
        Error error = new Error();
        error.setMessage("Access denied.");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

}
