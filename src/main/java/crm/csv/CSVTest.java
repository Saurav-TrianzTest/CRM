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
 * CSV processing utility.
 * Replaced java.io.File-based operations (cr-java-0063) with Amazon S3 client calls
 * using AWS SDK for Java v2 to achieve cloud-native, durable, and scalable storage
 * without host-level file system dependencies.
 */
public class CSVTest {

    public static void main(String[] args) {
        // Resolve S3 configuration from environment variables (12-factor app principle)
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME") != null
                ? System.getenv("AWS_S3_BUCKET_NAME")
                : "crm-csv-bucket";
        String s3Key = System.getenv("AWS_S3_CSV_KEY") != null
                ? System.getenv("AWS_S3_CSV_KEY")
                : "data/input.csv";

        // Build S3 client — credentials and region resolved from the environment
        // (IAM role, ~/.aws/credentials, or AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY env vars)
        S3Client s3Client = S3Client.builder().build();

        List<Object[]> data = new ArrayList<>();

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved S3 object: " + s3Key + " from bucket: " + bucketName);

            // Stream the S3 object content directly into the CSV reader — no local file needed
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object, StandardCharsets.UTF_8));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            reader.close();

        } catch (S3Exception e) {
            System.err.println("Failed to read CSV from S3 [" + s3Key + "]: "
                    + e.awsErrorDetails().errorMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }

}
