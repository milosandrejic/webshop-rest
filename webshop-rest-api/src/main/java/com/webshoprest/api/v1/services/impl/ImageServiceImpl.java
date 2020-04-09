package com.webshoprest.api.v1.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.webshoprest.api.v1.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${awsProperties.bucketName}")
    private String bucketName;

    @Value("${awsProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${imageExtension}")
    private String imageExtension;

    private AmazonS3 awsS3;

    @Autowired
    public ImageServiceImpl(AmazonS3 awsS3) {
        this.awsS3 = awsS3;
    }


    @Override
    public String uploadImage(File file) {

        awsS3.putObject(new PutObjectRequest(bucketName, file.getName(), file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        return buildImageUrl(file.getName());
    }

    @Override
    public void deleteImage(Long itemId) {
        awsS3.deleteObject(new DeleteObjectRequest(bucketName, itemId + imageExtension));
    }

    @Override
    public String updateImage(File file, Long itemId) {
        deleteImage(itemId);
        awsS3.putObject(new PutObjectRequest(bucketName, endpointUrl, file));
        return buildImageUrl(String.valueOf(itemId));
    }

    private String buildImageUrl(String filename){
        return endpointUrl + "/" + bucketName + "/" + filename;
    }

}
