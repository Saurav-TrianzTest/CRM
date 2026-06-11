package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for reading data from Amazon S3 instead of local file system.
 * Replaces hard-coded file path and JFileChooser-based local file access
 * with cloud-native S3 object storage operations using AWS SDK for Java v2.
 */
public class ReadDataUtils {

    /**
     * Downloads a file from Amazon S3 and returns it as a temporary local File.
     * The S3 bucket name is read from the environment variable S3_BUCKET_NAME,
     * and the object key is constructed from the provided key parameter.
     *
     * @param s3Client  an initialised AWS SDK v2 S3Client
     * @param s3Key     the S3 object key (e.g. "uploads/data.csv")
     * @param suffix    file suffix for the temporary file (e.g. ".csv")
     * @return a temporary {@link File} containing the downloaded object content,
     *         or {@code null} if the download fails
     */
    public static File readFileFromS3(S3Client s3Client, String s3Key, String suffix) {
        String bucketName = System.getenv("S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable S3_BUCKET_NAME is not set. " +
                    "Please configure it before running the application.");
        }

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            // Write the S3 stream to a temporary file so callers can use standard File APIs
            Path tempFile = Files.createTempFile("s3-download-", suffix);
            try (InputStream inputStream = s3Object) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("Downloaded S3 object: s3://" + bucketName + "/" + s3Key
                    + " -> " + tempFile.getFileName());
            return tempFile.toFile();

        } catch (S3Exception e) {
            System.err.println("Failed to read object from S3 [bucket=" + bucketName
                    + ", key=" + s3Key + "]: " + e.awsErrorDetails().errorMessage());
            return null;
        } catch (IOException e) {
            System.err.println("I/O error while downloading S3 object: " + e.getMessage());
            return null;
        }
    }

}
