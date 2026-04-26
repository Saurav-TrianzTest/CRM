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
 * Cloud-native CSV processing utility using Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
public class CSVTest {

    /**
     * Reads and processes CSV file from Amazon S3 instead of local file system.
     * 
     * @param s3Client AWS S3 client instance
     * @param bucketName S3 bucket name
     * @param s3Key S3 object key (path to CSV file in bucket)
     * @return List of parsed CSV rows
     * @throws IOException if S3 read or CSV parsing fails
     */
    public static List<Object[]> readCsvFromS3(S3Client s3Client, String bucketName, String s3Key) throws IOException {
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Retrieve CSV file from S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            
            // Parse CSV from S3 input stream
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                
                // Example business logic - filter specific records
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            reader.close();
            s3Object.close();
            
        } catch (S3Exception e) {
            System.err.println("Failed to read CSV from S3: " + e.getMessage());
            throw new IOException("S3 read error for bucket: " + bucketName + ", key: " + s3Key, e);
        } catch (IOException e) {
            System.err.println("CSV parsing error: " + e.getMessage());
            throw e;
        }
        
        return data;
    }

    /**
     * Main method for testing CSV processing from S3.
     * Configuration should be provided via environment variables:
     * - AWS_S3_BUCKET_NAME: S3 bucket name
     * - AWS_S3_CSV_KEY: S3 object key for the CSV file
     */
    public static void main(String[] args) {
        // Configuration from environment variables for cloud deployment
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String csvKey = System.getenv("AWS_S3_CSV_KEY");
        
        if (bucketName == null || csvKey == null) {
            System.err.println("ERROR: AWS_S3_BUCKET_NAME and AWS_S3_CSV_KEY environment variables must be set");
            System.err.println("Example: AWS_S3_BUCKET_NAME=my-bucket AWS_S3_CSV_KEY=data/file.csv");
            return;
        }
        
        // Create S3 client (uses default credential provider chain)
        S3Client s3Client = S3Client.builder().build();
        
        try {
            List<Object[]> data = readCsvFromS3(s3Client, bucketName, csvKey);
            System.out.println("Successfully processed " + data.size() + " rows from S3");
        } catch (IOException e) {
            System.err.println("Failed to process CSV from S3: " + e.getMessage());
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }
}
