package com.example.rrr.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String contentType = file.getContentType();

            // Создаем bucket если его нет
            createBucketIfNotExists();

            // Загружаем файл в MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage(), e);
        }
    }

    private void createBucketIfNotExists() {
        try {
            boolean found = minioClient.bucketExists(
                    io.minio.BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if (!found) {
                minioClient.makeBucket(
                        io.minio.MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании bucket: " + e.getMessage(), e);
        }
    }
}

