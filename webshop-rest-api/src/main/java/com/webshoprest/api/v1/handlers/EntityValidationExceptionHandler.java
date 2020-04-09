package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.EntityValidationException;
import com.webshoprest.api.v1.exceptions.NullIdException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EntityValidationExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<Object> handleEntityValidationException(EntityValidationException e, WebRequest request){
        return new ResponseEntity<>(e.getErrors(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullIdException.class)
    public ResponseEntity<Object> handleNullIdException(Exception e, WebRequest request){
        Error error = new Error();
        error.setMessage("id cannot be null");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
