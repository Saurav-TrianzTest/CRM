package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV processing utility migrated to use Amazon S3 for cloud-native storage.
 * Replaces java.io.File-based persistent storage operations with Amazon S3 client
 * calls to achieve cloud-native, durable, and scalable storage without host-level
 * file system dependencies.
 */
public class CSVTest {

    /**
     * Reads and processes a CSV file from Amazon S3 instead of the local file system.
     *
     * @param s3Client   the AWS S3 client
     * @param bucketName the S3 bucket name (configured via AWS_S3_BUCKET_NAME env variable)
     * @param s3Key      the S3 object key for the CSV file
     */
    public static void processCSVFromS3(S3Client s3Client, String bucketName, String s3Key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        List<Object[]> data = new ArrayList<>();
        try {
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object, StandardCharsets.UTF_8));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // S3 configuration from environment variables for cloud-native deployment
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String s3Key = System.getenv("CSV_S3_KEY");

        if (bucketName == null || bucketName.isEmpty()) {
            System.err.println("Environment variable AWS_S3_BUCKET_NAME is not set.");
            return;
        }
        if (s3Key == null || s3Key.isEmpty()) {
            System.err.println("Environment variable CSV_S3_KEY is not set.");
            return;
        }

        S3Client s3Client = S3Client.builder().build();
        processCSVFromS3(s3Client, bucketName, s3Key);
    }

}
