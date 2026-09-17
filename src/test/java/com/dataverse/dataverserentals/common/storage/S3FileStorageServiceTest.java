package com.dataverse.dataverserentals.common.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.dataverse.dataverserentals.common.config.S3Properties;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;

class S3FileStorageServiceTest {

    private static final String BUCKET = "test-bucket";
    private static final String REGION = "us-east-1";

    private S3Client s3Client;
    private S3FileStorageService service;

    @BeforeEach
    void setUp() {
        s3Client = mock(S3Client.class);
        var properties = new S3Properties(REGION, BUCKET, null, null, null, false);
        service = new S3FileStorageService(s3Client, properties);
    }

    @Test
    void shouldUploadFileAndReturnUrl() throws Exception {
        var content = "voucher-pdf-content";
        var file = new MockMultipartFile(
                "file", "voucher.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                content.getBytes());

        String url = service.uploadFile(file, "pagos/123");

        assertThat(url).startsWith("https://test-bucket.s3.us-east-1.amazonaws.com/pagos/123/");
        assertThat(url).endsWith("-voucher.pdf");
        verify(s3Client).putObject(any(Consumer.class), any(RequestBody.class));
    }

    @SuppressWarnings({ "unchecked", "resource" })
    @Test
    void shouldDownloadFile() throws Exception {
        var key = "pagos/456/uuid-test.pdf";
        var content = "file-content";
        var response = GetObjectResponse.builder().build();
        when(s3Client.getObject(any(Consumer.class)))
                .thenReturn(new ResponseInputStream<>(response, new ByteArrayInputStream(content.getBytes())));

        var result = service.downloadFile(key);
        assertThat(result).hasContent(content);
        result.close();
    }

    @Test
    void shouldDeleteFile() {
        var key = "pagos/789/uuid-delete.pdf";

        service.deleteFile(key);

        verify(s3Client).deleteObject(any(Consumer.class));
    }

    @Test
    void shouldReturnCorrectUrl() {
        var key = "pagos/1/uuid-file.pdf";

        String url = service.getFileUrl(key);

        assertThat(url).isEqualTo("https://test-bucket.s3.us-east-1.amazonaws.com/pagos/1/uuid-file.pdf");
    }

    @Test
    void shouldReturnLocalStackUrlWhenEndpointIsSet() {
        var localProperties = new S3Properties("us-east-1", BUCKET, "http://localhost:4566", "test", "test", true);
        var localService = new S3FileStorageService(s3Client, localProperties);

        String url = localService.getFileUrl("pagos/1/file.pdf");

        assertThat(url).isEqualTo("http://localhost:4566/test-bucket/pagos/1/file.pdf");
    }

    @Test
    void shouldThrowWhenS3FailsOnUpload() {
        var file = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "data".getBytes());
        doThrow(S3Exception.builder().message("S3 error").statusCode(500).build())
                .when(s3Client).putObject(any(Consumer.class), any(RequestBody.class));

        assertThatThrownBy(() -> service.uploadFile(file, "pagos/1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload file to S3");
    }

    @Test
    void shouldInitBucketWhenNotExists() {
        doThrow(NoSuchBucketException.builder().message("not found").build())
                .when(s3Client).headBucket(any(Consumer.class));

        service.initBucket();

        verify(s3Client).createBucket(any(Consumer.class));
    }

    @Test
    void shouldNotCreateBucketWhenAlreadyExists() {
        service.initBucket();

        verify(s3Client).headBucket(any(Consumer.class));
        verify(s3Client, never()).createBucket(any(Consumer.class));
    }
}
