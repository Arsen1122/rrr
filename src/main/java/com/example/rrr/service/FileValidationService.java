package com.example.rrr.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class FileValidationService {

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/png",
            "text/plain",
            "application/json"
    );

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "png",
            "txt",
            "json"
    );

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        // Проверка по расширению файла
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Неправильный тип файла. Разрешены только: PNG, TXT, JSON");
        }

        // Проверка по MIME типу (если доступен)
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Неправильный тип файла. Разрешены только: PNG, TXT, JSON");
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}

