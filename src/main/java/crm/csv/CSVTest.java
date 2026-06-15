package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV processing utility that reads CSV data from Amazon S3 instead of the local file system.
 * Replaces java.io.File-based storage operations with AWS SDK for Java v2 S3 client calls
 * to achieve cloud-native, durable, and scalable storage without host-level file system dependencies.
 */
public class CSVTest {

    /**
     * Reads a CSV file from Amazon S3 and processes its contents.
     *
     * @param s3Client   the AWS S3 client (AWS SDK v2)
     * @param bucketName the S3 bucket name
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
            System.out.println("Successfully processed " + data.size() + " rows from S3 object: " + s3Key);
        } catch (S3Exception e) {
            System.err.println("Failed to retrieve CSV from S3. Bucket: " + bucketName
                    + ", Key: " + s3Key + ", Error: " + e.awsErrorDetails().errorMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // S3 bucket and key are resolved from environment variables for cloud-native configuration
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
