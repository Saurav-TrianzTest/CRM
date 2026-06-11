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
 * CSV processing utility migrated to Amazon S3.
 * All java.io.File-based operations have been replaced with Amazon S3 client
 * calls using AWS SDK for Java v2 to achieve cloud-native, durable, and
 * scalable storage without host-level file system dependencies.
 *
 * The S3 bucket is configured via the S3_BUCKET_NAME environment variable
 * and the CSV object key is passed as a parameter.
 */
public class CSVTest {

    public static void main(String[] args) {
        String s3Key = System.getenv("CSV_S3_KEY");
        if (s3Key == null || s3Key.isEmpty()) {
            s3Key = "uploads/data.csv"; // default key; override via CSV_S3_KEY env var
        }

        String bucketName = System.getenv("S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable S3_BUCKET_NAME is not set. " +
                    "Please configure it before running the application.");
        }

        // Build an S3 client using the default credential/region provider chain
        S3Client s3Client = S3Client.create();

        List<Object[]> data = new ArrayList<>();

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            // Stream the S3 object directly into CSVReader — no local file required
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            try (CSVReader reader = new CSVReader(
                    new InputStreamReader(s3Object, StandardCharsets.UTF_8))) {

                String[] line;
                while ((line = reader.readNext()) != null) {
                    data.add(line);
                    if (line.length > 2 && line[1].equals("QUICK SUB")) {
                        System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                    }
                }
            }

            System.out.println("Processed " + data.size() + " rows from s3://"
                    + bucketName + "/" + s3Key);

        } catch (S3Exception e) {
            System.err.println("Failed to read CSV from S3 [bucket=" + bucketName
                    + ", key=" + s3Key + "]: " + e.awsErrorDetails().errorMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }

}
