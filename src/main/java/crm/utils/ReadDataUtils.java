package crm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
public class ReadDataUtils {

    /**
     * Reads a file from classpath resources for cloud deployment.
     * For cloud environments, files should be packaged within the application
     * or retrieved from cloud storage services like S3.
     *
     * @param resourcePath The classpath resource path (e.g., "data/file.csv")
     * @return File object pointing to the resource
     * @throws IOException if resource cannot be read
     */
    public static File readFileFromClasspath(String resourcePath) throws IOException {
        log.info("Reading file from classpath: {}", resourcePath);
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            log.error("Resource not found in classpath: {}", resourcePath);
            throw new IOException("Resource not found: " + resourcePath);
        }

        // Create a temporary file from the classpath resource
        InputStream inputStream = resource.getInputStream();
        Path tempFile = Files.createTempFile("cloud-resource-", resource.getFilename());
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        log.info("Copied classpath resource to temporary file: {}", tempFile);
        return tempFile.toFile();
    }

    /**
     * Reads a file from AWS S3 for cloud deployment.
     *
     * @param s3Client AWS S3 client
     * @param bucketName S3 bucket name
     * @param key S3 object key
     * @return File object pointing to the downloaded resource
     * @throws IOException if S3 object cannot be downloaded
     */
    public static File readFileFromS3(S3Client s3Client, String bucketName, String key) throws IOException {
        log.info("Reading file from S3: s3://{}/{}", bucketName, key);
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            InputStream inputStream = s3Client.getObject(getObjectRequest);
            String fileName = key.substring(key.lastIndexOf('/') + 1);
            Path tempFile = Files.createTempFile("s3-resource-", fileName);
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("Downloaded S3 object to temporary file: {}", tempFile);
            return tempFile.toFile();
        } catch (Exception e) {
            log.error("Failed to read file from S3: s3://{}/{}", bucketName, key, e);
            throw new IOException("Failed to read from S3: " + e.getMessage(), e);
        }
    }

}
