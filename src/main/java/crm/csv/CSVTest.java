package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and processes a CSV file stored in Amazon S3.
 *
 * Replaces the original java.io.File / JFileChooser approach with cloud-native
 * S3 object retrieval using AWS SDK for Java v2, eliminating host-level file
 * system dependencies.
 *
 * Required environment variables:
 *   AWS_S3_BUCKET_NAME  – the S3 bucket that contains the CSV file
 *   AWS_S3_CSV_KEY      – the S3 object key of the CSV file to process
 *   AWS_REGION          – the AWS region (defaults to us-east-1 if not set)
 */
public class CSVTest {

    public static void main(String[] args) {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String objectKey  = System.getenv("AWS_S3_CSV_KEY");
        String awsRegion  = System.getenv("AWS_REGION");

        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_BUCKET_NAME is not set.");
        }
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_CSV_KEY is not set.");
        }
        if (awsRegion == null || awsRegion.isEmpty()) {
            awsRegion = "us-east-1";
        }

        // Build an S3 client using the default credential provider chain
        // (IAM role, environment variables, ~/.aws/credentials, etc.)
        S3Client s3Client = S3Client.builder()
                .region(Region.of(awsRegion))
                .build();

        List<Object[]> data = new ArrayList<>();

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved S3 object: " + objectKey
                    + " from bucket: " + bucketName);

            // Stream the S3 object content directly into the CSV reader
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            reader.close();
        } catch (S3Exception e) {
            System.err.println("Failed to read S3 object '" + objectKey
                    + "' from bucket '" + bucketName + "': " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }

}
