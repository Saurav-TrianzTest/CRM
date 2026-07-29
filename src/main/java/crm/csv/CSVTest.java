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
    private static final String S3_CSV_KEY = System.getenv().getOrDefault("S3_CSV_KEY", "csv/data.csv");

    public static void main(String[] args) {
        try {
            processCSVFromS3(S3_CSV_KEY);
        } catch (IOException e) {
            System.err.println("Failed to process CSV from S3: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads and processes CSV file directly from Amazon S3.
     * No local file system dependency - streams data from cloud storage.
     * 
     * @param s3Key The S3 object key for the CSV file
     * @throws IOException if S3 read fails
     */
    public static void processCSVFromS3(String s3Key) throws IOException {
        S3Client s3Client = S3Client.builder().build();
        
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            
            // Process CSV directly from S3 stream
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            List<Object[]> data = new ArrayList<>();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                
                // Example processing logic
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            reader.close();
            s3Object.close();
            
            System.out.println("Successfully processed " + data.size() + " rows from S3: " + s3Key);
            
        } catch (S3Exception e) {
            System.err.println("S3 error: " + e.getMessage());
            throw new IOException("Failed to read CSV from S3: " + s3Key, e);
        } finally {
            s3Client.close();
        }
    }

    /**
     * Alternative method to process CSV with custom S3 bucket and key.
     * 
     * @param bucketName S3 bucket name
     * @param s3Key S3 object key
     * @return List of parsed CSV rows
     * @throws IOException if S3 read fails
     */
    public static List<String[]> readCSVFromS3(String bucketName, String s3Key) throws IOException {
        S3Client s3Client = S3Client.builder().build();
        List<String[]> data = new ArrayList<>();
        
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
            
            reader.close();
            s3Object.close();
            
        } catch (S3Exception e) {
            throw new IOException("Failed to read CSV from S3: " + s3Key, e);
        } finally {
            s3Client.close();
        }
        
        return data;
    }
}
