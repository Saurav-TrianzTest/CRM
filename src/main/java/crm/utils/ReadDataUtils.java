package crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Cloud-native file handling utilities for AWS S3
 * Replaces desktop GUI file chooser with cloud storage operations
 */
public class ReadDataUtils {

    private static final Logger log = LoggerFactory.getLogger(ReadDataUtils.class);

    /**
     * Upload file to S3 bucket from MultipartFile (web upload)
     * @param multipartFile - File uploaded via web form
     * @param s3Client - AWS S3 client
     * @param bucketName - S3 bucket name
     * @param s3Key - S3 object key (path)
     * @return S3 object key
     */
    public static String uploadFileToS3(MultipartFile multipartFile, S3Client s3Client,
                                       String bucketName, String s3Key) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(multipartFile.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                multipartFile.getInputStream(), multipartFile.getSize()));

        log.info("File uploaded to S3: s3://{}/{}", bucketName, s3Key);
        return s3Key;
    }

    /**
     * Download file from S3 to temporary location for processing
     * @param s3Client - AWS S3 client
     * @param bucketName - S3 bucket name
     * @param s3Key - S3 object key
     * @return Path to temporary file
     */
    public static Path downloadFileFromS3(S3Client s3Client, String bucketName, String s3Key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        InputStream inputStream = s3Client.getObject(getObjectRequest);
        Path tempFile = Files.createTempFile("s3-download-", ".tmp");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        log.info("File downloaded from S3: s3://{}/{} to {}", bucketName, s3Key, tempFile);
        return tempFile;
    }

    /**
     * Read file from classpath resources (for bundled files)
     * @param resourcePath - Classpath resource path
     * @return InputStream
     */
    public static InputStream readFromClasspath(String resourcePath) {
        InputStream inputStream = ReadDataUtils.class.getClassLoader()
                .getResourceAsStream(resourcePath);
        if (inputStream == null) {
            log.error("Resource not found in classpath: {}", resourcePath);
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        log.info("Reading file from classpath: {}", resourcePath);
        return inputStream;
    }

}
