package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV processing utility using Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
public class CSVTest {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    private static final String AWS_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

    /**
     * Reads and processes a CSV file from Amazon S3.
     *
     * @param s3Key The S3 object key (path within the bucket) for the CSV file
     * @return List of parsed CSV rows
     */
    public static List<Object[]> readCsvFromS3(String s3Key) {
        List<Object[]> data = new ArrayList<>();

        try {
            S3Client s3Client = S3Client.builder()
                    .region(software.amazon.awssdk.regions.Region.of(AWS_REGION))
                    .build();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            // Read CSV directly from S3 stream without writing to local file system
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            reader.close();

            System.out.println("Successfully processed CSV from S3: " + s3Key);
            System.out.println("Total rows processed: " + data.size());

        } catch (S3Exception e) {
            System.err.println("S3 error while reading CSV: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO error while processing CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    public static void main(String[] args) {
        // Cloud-ready implementation: Read CSV file from S3
        // S3 key should be provided via environment variable or command line argument
        String s3Key = System.getenv().getOrDefault("CSV_S3_KEY", "csv-files/sample.csv");

        if (args.length > 0) {
            s3Key = args[0];
        }

        System.out.println("Reading CSV from S3: s3://" + S3_BUCKET_NAME + "/" + s3Key);
        List<Object[]> data = readCsvFromS3(s3Key);

        // Example: Print first two rows if available
        if (data.size() > 0) {
            Object[] firstRow = data.get(0);
            if (firstRow.length > 2) {
                System.out.println("First row: " + firstRow[1] + "\t" + firstRow[2]);
            }
        }
        if (data.size() > 1) {
            Object[] secondRow = data.get(1);
            if (secondRow.length > 2) {
                System.out.println("Second row: " + secondRow[1] + "\t" + secondRow[2]);
            }
        }
    }

}
