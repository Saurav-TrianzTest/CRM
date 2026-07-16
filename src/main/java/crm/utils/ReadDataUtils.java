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
 * Replaces hard-coded file path / JFileChooser-based file selection with
 * cloud-native S3 object retrieval (cr-java-0061).
 */
public class ReadDataUtils {

    /**
     * Reads a file from Amazon S3 and returns its InputStream.
     *
     * @param s3Client   an initialised S3Client (AWS SDK v2)
     * @param bucketName the S3 bucket name (resolved from environment variable AWS_S3_BUCKET_NAME)
     * @param s3Key      the S3 object key (path within the bucket)
     * @return InputStream of the S3 object content, or null if the object could not be retrieved
     */
    public static InputStream readFileFromS3(S3Client s3Client, String bucketName, String s3Key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved S3 object: " + s3Key + " from bucket: " + bucketName);
            return s3Object;
        } catch (S3Exception e) {
            System.err.println("Failed to read S3 object [" + s3Key + "]: " + e.awsErrorDetails().errorMessage());
            return null;
        }
    }

    /**
     * Convenience overload that resolves the bucket name from the environment variable
     * AWS_S3_BUCKET_NAME, falling back to a provided default.
     *
     * @param s3Client         an initialised S3Client (AWS SDK v2)
     * @param s3Key            the S3 object key
     * @param defaultBucketName fallback bucket name when the environment variable is absent
     * @return InputStream of the S3 object content, or null if the object could not be retrieved
     */
    public static InputStream readFileFromS3(S3Client s3Client, String s3Key, String defaultBucketName) {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME") != null
                ? System.getenv("AWS_S3_BUCKET_NAME")
                : defaultBucketName;
        return readFileFromS3(s3Client, bucketName, s3Key);
    }

}
