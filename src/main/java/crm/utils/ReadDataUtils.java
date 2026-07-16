package crm.utils;

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
 * Utility class for reading data from Amazon S3 instead of local file system.
 * Replaces the previous JFileChooser / local File-based approach with
 * cloud-native S3 object storage to eliminate host file-system dependencies.
 */
public class ReadDataUtils {

    /**
     * Retrieves an object from Amazon S3 as an InputStream.
     *
     * @param s3Client   an initialised AWS SDK v2 S3Client
     * @param bucketName the S3 bucket that contains the object
     * @param objectKey  the S3 object key (path) of the file to read
     * @return InputStream of the S3 object content, or null if not found
     */
    public static InputStream readFileFromS3(S3Client s3Client, String bucketName, String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Reading object from S3: s3://" + bucketName + "/" + objectKey);
            return s3Object;
        } catch (Exception e) {
            System.err.println("Failed to read object from S3: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lists objects in an S3 bucket filtered by a given prefix and file extension.
     *
     * @param s3Client             an initialised AWS SDK v2 S3Client
     * @param bucketName           the S3 bucket to list objects from
     * @param prefix               the key prefix (folder path) to filter objects
     * @param fileExtensionFilter  file extension to filter (e.g. "csv"), or null for all
     * @return list of matching S3 object keys
     */
    public static List<String> listFilesFromS3(S3Client s3Client, String bucketName,
                                               String prefix, String fileExtensionFilter) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix != null ? prefix : "")
                .build();
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
        List<String> keys = listResponse.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
        if (fileExtensionFilter != null && !fileExtensionFilter.isEmpty()) {
            String ext = fileExtensionFilter.toLowerCase();
            keys = keys.stream()
                    .filter(k -> k.toLowerCase().endsWith("." + ext))
                    .collect(Collectors.toList());
        }
        return keys;
    }

}
