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
     * Downloads a file from S3 and returns it as a temporary File object.
     * 
     * @param s3Key The S3 object key (path within the bucket)
     * @param fileExtension Expected file extension for validation
     * @return File object pointing to the downloaded temporary file
     * @throws IOException if download fails
     */
    public static File readFileFromS3(String s3Key, String fileExtension) throws IOException {
        if (!s3Key.endsWith("." + fileExtension)) {
            throw new IllegalArgumentException("File must have extension: " + fileExtension);
        }

        S3Client s3Client = S3Client.builder().build();
        
        try {
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
            System.err.println("Failed to download file from S3: " + e.getMessage());
            throw new IOException("S3 download failed for key: " + s3Key, e);
        } finally {
            s3Client.close();
        }
    }

    /**
     * Reads file content directly from S3 as an InputStream.
     * More efficient for streaming operations without local file creation.
     * 
     * @param s3Key The S3 object key
     * @return InputStream of the S3 object content
     * @throws IOException if download fails
     */
    public static InputStream readStreamFromS3(String s3Key) throws IOException {
        S3Client s3Client = S3Client.builder().build();
        
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            return s3Client.getObject(getObjectRequest);
            
        } catch (S3Exception e) {
            s3Client.close();
            System.err.println("Failed to read stream from S3: " + e.getMessage());
            throw new IOException("S3 stream read failed for key: " + s3Key, e);
        }
    }
}
