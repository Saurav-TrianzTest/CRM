package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-ready utility for reading data from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
public class ReadDataUtils {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    private static final String AWS_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

    /**
     * Reads a file from Amazon S3 and returns it as a temporary local file.
     * This method replaces the previous JFileChooser-based file selection which was
     * incompatible with cloud environments.
     *
     * @param s3Key The S3 object key (path within the bucket)
     * @param fileExtension Expected file extension for validation
     * @return File object pointing to a temporary local copy, or null if operation fails
     */
    public static File readFileFromS3(String s3Key, String fileExtension) {
        if (s3Key == null || s3Key.isEmpty()) {
            System.err.println("S3 key cannot be null or empty");
            return null;
        }

        // Validate file extension
        if (fileExtension != null && !s3Key.endsWith("." + fileExtension)) {
            System.err.println("File does not match expected extension: " + fileExtension);
            return null;
        }

        try {
            S3Client s3Client = S3Client.builder()
                    .region(software.amazon.awssdk.regions.Region.of(AWS_REGION))
                    .build();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            // Create temporary file
            String fileName = s3Key.substring(s3Key.lastIndexOf('/') + 1);
            File tempFile = File.createTempFile("s3-download-", "-" + fileName);
            tempFile.deleteOnExit();

            // Write S3 content to temporary file
            try (FileOutputStream fos = new FileOutputStream(tempFile);
                 InputStream is = s3Object) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Successfully downloaded file from S3: " + s3Key);
            return tempFile;

        } catch (S3Exception e) {
            System.err.println("S3 error while reading file: " + e.awsErrorDetails().errorMessage());
            return null;
        } catch (IOException e) {
            System.err.println("IO error while processing S3 file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Legacy method signature maintained for backward compatibility.
     * Now delegates to S3-based implementation.
     *
     * @deprecated Use readFileFromS3 directly with S3 key
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        // For cloud environments, this method should be called with S3 key as dialogMessage
        // and first fileExtension element
        String s3Key = dialogMessage;
        String extension = (fileExtension != null && fileExtension.length > 0) ? fileExtension[0] : null;
        return readFileFromS3(s3Key, extension);
    }

}
