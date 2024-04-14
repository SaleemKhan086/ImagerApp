package com.imager.imagerapp.controller;

import com.imager.imagerapp.service.impl.S3ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3ImageUploader s3ImageUploaderService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file){
        return ResponseEntity.ok(s3ImageUploaderService.uploadImage(file));
    }

    @GetMapping
    public ResponseEntity<?> getAllFiles(){
        return ResponseEntity.ok(s3ImageUploaderService.allFiles());
    }

}
