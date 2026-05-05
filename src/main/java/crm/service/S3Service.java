package crm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

/**
 * Service for managing file operations with Amazon S3.
 * Provides cloud-native, durable, and scalable storage without local file system dependencies.
 */
@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
        this.bucketName = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    }

    /**
     * Upload a file to S3.
     *
     * @param key The S3 object key (path)
     * @param content The file content as byte array
     * @param contentType The MIME type of the content
     */
    public void uploadFile(String key, byte[] content, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
            log.info("Successfully uploaded file to S3: s3://{}/{}", bucketName, key);
        } catch (Exception e) {
            log.error("Failed to upload file to S3: {}", key, e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    /**
     * Download a file from S3.
     *
     * @param key The S3 object key (path)
     * @return InputStream of the file content
     */
    public InputStream downloadFile(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            log.info("Successfully downloaded file from S3: s3://{}/{}", bucketName, key);
            return s3Object;
        } catch (Exception e) {
            log.error("Failed to download file from S3: {}", key, e);
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    /**
     * Get the configured S3 bucket name.
     *
     * @return The S3 bucket name
     */
    public String getBucketName() {
        return bucketName;
    }
}
