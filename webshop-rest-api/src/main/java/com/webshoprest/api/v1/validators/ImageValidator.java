package com.webshoprest.api.v1.validators;

import com.webshoprest.api.v1.exceptions.EmptyImageException;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator{

    public static void validateImage(MultipartFile image) {

        if(image.isEmpty()){
            throw new EmptyImageException();
        }

    }
}
