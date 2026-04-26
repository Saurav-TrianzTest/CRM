package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-native utility for reading data from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
public class ReadDataUtils {

    private final S3Client s3Client;
    private final String bucketName;

    /**
     * Constructor with S3 client and bucket name from environment configuration.
     * 
     * @param s3Client AWS S3 client instance
     * @param bucketName S3 bucket name (should be configured via environment variable)
     */
    public ReadDataUtils(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    /**
     * Reads a file from Amazon S3 instead of local file system.
     * 
     * @param s3Key The S3 object key (path within the bucket)
     * @return InputStream of the S3 object content
     * @throws IOException if the object cannot be retrieved
     */
    public InputStream readFileFromS3(String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return s3Object;
        } catch (S3Exception e) {
            throw new IOException("Failed to read file from S3: " + s3Key, e);
        }
    }

    /**
     * Reads a file from S3 with custom bucket name.
     * 
     * @param bucketName Custom S3 bucket name
     * @param s3Key The S3 object key
     * @return InputStream of the S3 object content
     * @throws IOException if the object cannot be retrieved
     */
    public InputStream readFileFromS3(String bucketName, String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return s3Object;
        } catch (S3Exception e) {
            throw new IOException("Failed to read file from S3 bucket " + bucketName + ", key: " + s3Key, e);
        }
    }
}
