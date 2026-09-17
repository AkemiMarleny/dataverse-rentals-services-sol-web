package com.dataverse.dataverserentals.common.storage;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String uploadFile(MultipartFile file, String directory);

    InputStream downloadFile(String key);

    void deleteFile(String key);

    String getFileUrl(String key);
}
