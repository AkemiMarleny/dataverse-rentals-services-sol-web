package com.dataverse.dataverserentals.common.storage;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dataverse.dataverserentals.common.config.S3Properties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;
    private final S3Properties properties;

    @PostConstruct
    void initBucket() {
        try {
            s3Client.headBucket(b -> b.bucket(properties.bucket()));
            log.info("Bucket '{}' already exists", properties.bucket());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(b -> b.bucket(properties.bucket()));
            log.info("Bucket '{}' created", properties.bucket());
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String directory) {
        String key = "%s/%s-%s".formatted(
                directory,
                UUID.randomUUID(),
                file.getOriginalFilename());

        try (var is = file.getInputStream()) {
            s3Client.putObject(
                    b -> b.bucket(properties.bucket()).key(key),
                    RequestBody.fromInputStream(is, file.getSize()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        log.info("File uploaded to S3: bucket={}, key={}", properties.bucket(), key);
        return getFileUrl(key);
    }

    @Override
    public InputStream downloadFile(String key) {
        try {
            return s3Client.getObject(b -> b
                    .bucket(properties.bucket())
                    .key(key));
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to download file from S3: " + key, e);
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(b -> b
                    .bucket(properties.bucket())
                    .key(key));
            log.info("File deleted from S3: bucket={}, key={}", properties.bucket(), key);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to delete file from S3: " + key, e);
        }
    }

    @Override
    public String getFileUrl(String key) {
        if (StringUtils.hasText(properties.endpoint())) {
            return "%s/%s/%s".formatted(
                    properties.endpoint().replaceAll("/$", ""),
                    properties.bucket(),
                    key);
        }
        return "https://%s.s3.%s.amazonaws.com/%s".formatted(
                properties.bucket(),
                properties.region(),
                key);
    }
}
