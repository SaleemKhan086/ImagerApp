package com.imager.imagerapp.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.imager.imagerapp.exceptions.ImageUploadException;
import com.imager.imagerapp.service.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3ImageUploader implements ImageUploader {

    @Autowired
    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketname;

    @Override
    public String uploadImage(MultipartFile image) {

        if (Objects.isNull(image)){
            throw new ImageUploadException("image is null!!");
        }

        String originalFilename = image.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        try {
            PutObjectRequest request = new PutObjectRequest(bucketname, filename, image.getInputStream(), metadata);
            PutObjectResult result = client.putObject(request);
            return this.preSignedUrl(filename);
        } catch (IOException e) {
            throw new ImageUploadException(e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {
        ListObjectsV2Result listObjectsV2Result = client.listObjectsV2(bucketname);
        return listObjectsV2Result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .map(this::preSignedUrl)
                .collect(Collectors.toList());
    }

    @Override
    public String preSignedUrl(String filename) {
        Date exirationDate = new Date();
        long timeMillis = exirationDate.getTime();
        timeMillis = timeMillis + 2*60*60*1000; //add 2 hrs
        exirationDate.setTime(timeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketname, filename)
            .withMethod(HttpMethod.GET)
            .withExpiration(exirationDate);

        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
