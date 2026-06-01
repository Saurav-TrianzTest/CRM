package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;

/**
 * Utility class for reading data from Amazon S3 instead of local file system paths.
 * Replaces hard-coded file path dependencies with cloud-native S3 object storage access.
 */
public class ReadDataUtils {

    /**
     * Reads a file from Amazon S3 and returns its InputStream.
     *
     * @param s3Client   the AWS S3 client
     * @param bucketName the S3 bucket name (from environment variable AWS_S3_BUCKET_NAME)
     * @param s3Key      the S3 object key (path within the bucket)
     * @return InputStream of the S3 object content, or null if not found
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
            System.err.println("Failed to read file from S3: " + e.awsErrorDetails().errorMessage());
            return null;
        }
    }

    /**
     * Returns the configured S3 bucket name from environment variables.
     *
     * @return S3 bucket name
     */
    public static String getS3BucketName() {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = System.getProperty("aws.s3.bucketName", "crm-default-bucket");
        }
        return bucketName;
    }

}
