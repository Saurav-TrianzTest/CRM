package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for reading data from Amazon S3 instead of local file system.
 * Replaces the original JFileChooser / hard-coded file path approach with
 * cloud-native S3 object retrieval using AWS SDK for Java v2.
 */
public class ReadDataUtils {

    /**
     * Reads an object from Amazon S3 and returns its InputStream.
     *
     * @param s3Client   An initialised S3Client (AWS SDK v2)
     * @param bucketName The S3 bucket name (typically supplied via the
     *                   environment variable AWS_S3_BUCKET_NAME)
     * @param objectKey  The S3 object key (path within the bucket)
     * @return InputStream of the S3 object content, or null if not found
     */
    public static InputStream readFileFromS3(S3Client s3Client, String bucketName, String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved S3 object: " + objectKey + " from bucket: " + bucketName);
            return s3Object;
        } catch (S3Exception e) {
            System.err.println("Failed to read S3 object '" + objectKey + "' from bucket '" + bucketName + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Convenience overload that resolves the bucket name from the environment
     * variable AWS_S3_BUCKET_NAME.
     *
     * @param s3Client  An initialised S3Client (AWS SDK v2)
     * @param objectKey The S3 object key (path within the bucket)
     * @return InputStream of the S3 object content, or null if not found
     */
    public static InputStream readFileFromS3(S3Client s3Client, String objectKey) {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_BUCKET_NAME is not set. " +
                    "Please configure it before using S3 file operations.");
        }
        return readFileFromS3(s3Client, bucketName, objectKey);
    }
}
