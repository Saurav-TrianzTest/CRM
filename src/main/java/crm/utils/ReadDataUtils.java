import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

@Component
    private final S3Client s3Client;
    private final String bucketName;

    /**
     * Constructor with S3 client and bucket name.
    public ReadDataUtils(S3Client s3Client, 
                         @Value("${aws.s3.bucket.name:crm-pdf-bucket}") String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * Reads a file from S3 bucket.
     * 
     * @param s3Key The S3 object key (path within bucket)
     * @return InputStream of the S3 object content
     * @throws IOException if the object cannot be retrieved
     */
    public InputStream readFileFromS3(String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return s3Object;
        } catch (S3Exception e) {
            throw new IOException("Failed to read file from S3: " + s3Key, e);
        }
    }

    /**
     * Reads a file from S3 with custom bucket.
     * 
     * @param customBucket Custom S3 bucket name
     * @param s3Key The S3 object key
     * @return InputStream of the S3 object content
     * @throws IOException if the object cannot be retrieved
     */
    public InputStream readFileFromS3(String customBucket, String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(customBucket)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return s3Object;
        } catch (S3Exception e) {
            throw new IOException("Failed to read file from S3 bucket " + customBucket + ": " + s3Key, e);
        }
    }
}
