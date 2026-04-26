package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
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
 * Cloud-ready CSV processor that reads CSV files from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
public class CSVTest {

    /**
     * Reads and processes CSV file from Amazon S3.
     * 
     * @param bucketName S3 bucket name (should be from environment variable)
     * @param s3Key S3 object key (path to CSV file in bucket)
     * @return List of parsed CSV rows
     * @throws IOException if S3 read fails
     */
    public static List<Object[]> readCsvFromS3(String bucketName, String s3Key) throws IOException {
        // Initialize S3 client with default credentials provider (uses IAM roles in cloud)
        S3Client s3Client = S3Client.builder()
                .region(Region.of(System.getenv().getOrDefault("AWS_REGION", "us-east-1")))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        List<Object[]> data = new ArrayList<>();
        
        try {
            // Retrieve CSV file from S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            
            // Parse CSV from S3 stream
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            reader.close();
            s3Object.close();
            
        } catch (S3Exception e) {
            throw new IOException("Failed to read CSV from S3: s3://" + bucketName + "/" + s3Key, e);
        } catch (IOException e) {
            throw new IOException("Failed to parse CSV file from S3", e);
        } finally {
            s3Client.close();
        }
        
        return data;
    }

    /**
     * Main method for testing CSV processing from S3.
     * Bucket name and S3 key should be provided via environment variables or command line arguments.
     */
    public static void main(String[] args) {
        // Get S3 configuration from environment variables
        String bucketName = System.getenv().getOrDefault("CSV_BUCKET_NAME", "crm-csv-bucket");
        String s3Key = System.getenv().getOrDefault("CSV_S3_KEY", "csv-files/sample.csv");
        
        // Override with command line arguments if provided
        if (args.length >= 2) {
            bucketName = args[0];
            s3Key = args[1];
        }
        
        try {
            System.out.println("Reading CSV from S3: s3://" + bucketName + "/" + s3Key);
            List<Object[]> data = readCsvFromS3(bucketName, s3Key);
            System.out.println("Successfully processed " + data.size() + " rows from S3");
        } catch (IOException e) {
            System.err.println("Error processing CSV from S3: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
