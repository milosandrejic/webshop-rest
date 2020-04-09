package com.webshoprest.api.v1.handlers;

import com.webshoprest.api.v1.exceptions.EmptyImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ImageExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmptyImageException.class)
    public ResponseEntity<Error> handlerEmptyImageException(EmptyImageException e){
        Error error = new Error();
        error.setMessage("Image that you try to upload is empty.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
