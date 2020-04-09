package com.webshoprest.api.v1.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileConverter {

    @Value("${imageExtension}")
    private static String imageExtension;

    public static File convertFromMultipartToFile(MultipartFile multipartFile, Long itemId) throws IOException {
        File file  = new File(itemId + (imageExtension == null ? ".jpg" : imageExtension));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }

}
