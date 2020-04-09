package com.webshoprest.api.v1.services;

import java.io.File;

public interface ImageService {

    public String uploadImage(File file);

    public void deleteImage(Long itemId);

    public String updateImage(File file, Long itemId);
}
