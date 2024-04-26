package com.imager.imagerapp.controller;

import com.imager.imagerapp.service.impl.S3ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3ImageUploader s3ImageUploaderService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file){
        log.info("Received request to upload file: " + file.getOriginalFilename());
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return new ResponseEntity(s3ImageUploaderService.uploadImage(file), headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllFiles(){
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return new ResponseEntity(s3ImageUploaderService.allFiles(), headers, HttpStatus.OK);
    }

}
