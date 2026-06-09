package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;

/**
 * Utility class for reading data from Amazon S3 instead of local file system.
 * Replaces the previous JFileChooser / hard-coded file path approach with
 * cloud-native S3 object storage access (AWS SDK for Java v2).
 */
public class ReadDataUtils {

    /**
     * Reads an object from Amazon S3 and returns its InputStream.
     *
     * @param bucketName  S3 bucket name (resolved from environment variable AWS_S3_BUCKET_NAME)
     * @param objectKey   S3 object key (path within the bucket, e.g. "data/input.csv")
     * @param s3Client    Pre-configured AWS SDK v2 S3Client instance
     * @return InputStream of the S3 object content, or null if the object cannot be retrieved
     */
    public static InputStream readFileFromS3(String bucketName, String objectKey, S3Client s3Client) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved S3 object: s3://" + bucketName + "/" + objectKey);
            return s3Object;
        } catch (S3Exception e) {
            System.err.println("Failed to read S3 object s3://" + bucketName + "/" + objectKey
                    + " - " + e.awsErrorDetails().errorMessage());
            return null;
        }
    }

    /**
     * Convenience overload that resolves the bucket name from the
     * AWS_S3_BUCKET_NAME environment variable.
     *
     * @param objectKey S3 object key
     * @param s3Client  Pre-configured AWS SDK v2 S3Client instance
     * @return InputStream of the S3 object content, or null on failure
     */
    public static InputStream readFileFromS3(String objectKey, S3Client s3Client) {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_BUCKET_NAME is not set. "
                    + "Please configure it before reading from S3.");
        }
        return readFileFromS3(bucketName, objectKey, s3Client);
    }
}
