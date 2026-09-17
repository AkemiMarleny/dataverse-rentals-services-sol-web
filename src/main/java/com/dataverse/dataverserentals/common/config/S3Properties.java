package com.dataverse.dataverserentals.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dataverse.s3")
public record S3Properties(
        String region,
        String bucket,
        String endpoint,
        String accessKeyId,
        String secretAccessKey,
        boolean pathStyleAccess) {
}
