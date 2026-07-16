package crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for reading data from Amazon S3.
 * Replaces the previous hard-coded file path / JFileChooser-based approach
 * with cloud-native S3 object storage access using AWS SDK for Java v2.
 */
public class ReadDataUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReadDataUtils.class);

    /**
     * Reads a file from Amazon S3 and returns its InputStream.
     *
     * @param s3Client   an initialised AWS SDK v2 S3Client
     * @param bucketName the S3 bucket name (typically supplied via the
     *                   {@code AWS_S3_BUCKET_NAME} environment variable)
     * @param objectKey  the S3 object key (path within the bucket)
     * @return InputStream of the S3 object content, or {@code null} if the
     *         object could not be retrieved
     */
    public static InputStream readFileFromS3(S3Client s3Client, String bucketName, String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            logger.info("Successfully retrieved S3 object: s3://{}/{}", bucketName, objectKey);
            return s3Object;
        } catch (Exception e) {
            logger.error("Failed to read file from S3: s3://{}/{} - {}", bucketName, objectKey, e.getMessage());
            return null;
        }
    }

    /**
     * Lists S3 objects in a bucket whose keys end with the given file extension.
     *
     * @param s3Client              an initialised AWS SDK v2 S3Client
     * @param bucketName            the S3 bucket name
     * @param fileExtensionDescription description label (informational only)
     * @param fileExtensions        one or more file extensions to filter by (e.g. "csv")
     * @return list of matching S3 object keys
     */
    public static List<String> listFilesFromS3(S3Client s3Client, String bucketName,
                                               String fileExtensionDescription,
                                               String... fileExtensions) {
        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
            List<String> matchingKeys = listResponse.contents().stream()
                    .map(S3Object::key)
                    .filter(key -> {
                        for (String ext : fileExtensions) {
                            if (key.toLowerCase().endsWith("." + ext.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            logger.info("Found {} {} file(s) in S3 bucket '{}'", matchingKeys.size(),
                    fileExtensionDescription, bucketName);
            return matchingKeys;
        } catch (Exception e) {
            logger.error("Failed to list files from S3 bucket '{}': {}", bucketName, e.getMessage());
            return List.of();
        }
    }
}
