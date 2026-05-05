package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.regions.Region;

import java.io.InputStream;

/**
 * Cloud-ready utility for reading data from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
public class ReadDataUtils {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    private static final String AWS_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

    /**
     * Reads a file from Amazon S3 instead of local file system.
     * 
     * @param s3Key The S3 object key (path) to read
     * @return InputStream of the S3 object content
     */
    public static InputStream readFileFromS3(String s3Key) {
        try {
            S3Client s3Client = S3Client.builder()
                    .region(Region.of(AWS_REGION))
                    .build();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return s3Object;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file from S3: " + s3Key, e);
        }
    }

    /**
     * Legacy method maintained for backward compatibility.
     * Now reads from S3 instead of local file system.
     * 
     * @param s3Key The S3 object key to read
     * @return InputStream of the file content
     */
    public static InputStream ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                       String... fileExtension) {
        // In cloud environment, file selection dialogs are not supported
        // This method now expects s3Key to be passed as dialogMessage parameter
        String s3Key = dialogMessage;
        return readFileFromS3(s3Key);
    }

}
