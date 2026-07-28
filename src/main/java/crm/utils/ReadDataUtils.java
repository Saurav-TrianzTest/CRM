package crm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-native utility for reading data from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
@Component
public class ReadDataUtils {

    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket.name:default-bucket}")
    private String bucketName;

    public ReadDataUtils(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Reads a file from Amazon S3 bucket.
     * 
     * @param s3Key The S3 object key (path within the bucket)
     * @return InputStream of the S3 object content
     * @throws IOException if the object cannot be read
     */
    public InputStream readFileFromS3(String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            
            // Read the entire stream into a byte array to avoid connection issues
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int bytesRead;
            while ((bytesRead = s3Object.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            s3Object.close();
            
            return new java.io.ByteArrayInputStream(buffer.toByteArray());
        } catch (Exception e) {
            throw new IOException("Failed to read file from S3: " + s3Key, e);
        }
    }

    /**
     * Reads a file from Amazon S3 bucket with custom bucket name.
     * 
     * @param bucketName The S3 bucket name
     * @param s3Key The S3 object key (path within the bucket)
     * @return InputStream of the S3 object content
     * @throws IOException if the object cannot be read
     */
    public InputStream readFileFromS3(String bucketName, String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            
            // Read the entire stream into a byte array to avoid connection issues
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int bytesRead;
            while ((bytesRead = s3Object.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            s3Object.close();
            
            return new java.io.ByteArrayInputStream(buffer.toByteArray());
        } catch (Exception e) {
            throw new IOException("Failed to read file from S3: " + s3Key, e);
        }
    }
}
