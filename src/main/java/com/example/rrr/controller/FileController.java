package com.example.rrr.controller;

import com.example.rrr.service.FileValidationService;
import com.example.rrr.service.MinioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileValidationService fileValidationService;
    private final MinioService minioService;

    public FileController(FileValidationService fileValidationService, MinioService minioService) {
        this.fileValidationService = fileValidationService;
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {

            fileValidationService.validateFile(file);
            

            String fileName = minioService.uploadFile(file);
            
            response.put("success", true);
            response.put("message", "Файл успешно загружен");
            response.put("fileName", fileName);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при загрузке файла: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

