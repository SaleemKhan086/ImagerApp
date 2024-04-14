package com.imager.imagerapp.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleImageUploadException(ImageUploadException imageUploadException){
        return ResponseEntity.internalServerError().build();
    }
}
