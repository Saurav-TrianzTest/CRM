package crm.csv;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-native CSV reader that reads from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
@Component
public class CSVTest {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name:default-bucket}")
    private String bucketName;

    public CSVTest(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Reads CSV data from Amazon S3 instead of local file system.
     * 
     * @param s3Key The S3 object key (path within the bucket) for the CSV file
     * @return List of CSV rows as Object arrays
     * @throws IOException if the CSV cannot be read
     */
    public List<Object[]> readCsvFromS3(String s3Key) throws IOException {
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            // Read CSV directly from S3 stream
            reader = new CSVReader(new InputStreamReader(s3Client.getObject(getObjectRequest)));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            System.out.println("Successfully read " + data.size() + " rows from S3: " + s3Key);
            
        } catch (IOException e) {
            System.err.println("Failed to read CSV from S3: " + s3Key);
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Failed to close CSV reader");
                }
            }
        }
        
        return data;
    }

    /**
     * Reads CSV data from Amazon S3 with custom bucket name.
     * 
     * @param bucketName The S3 bucket name
     * @param s3Key The S3 object key (path within the bucket) for the CSV file
     * @return List of CSV rows as Object arrays
     * @throws IOException if the CSV cannot be read
     */
    public List<Object[]> readCsvFromS3(String bucketName, String s3Key) throws IOException {
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            // Read CSV directly from S3 stream
            reader = new CSVReader(new InputStreamReader(s3Client.getObject(getObjectRequest)));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            System.out.println("Successfully read " + data.size() + " rows from S3: s3://" + bucketName + "/" + s3Key);
            
        } catch (IOException e) {
            System.err.println("Failed to read CSV from S3: s3://" + bucketName + "/" + s3Key);
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Failed to close CSV reader");
                }
            }
        }
        
        return data;
    }

    /**
     * Example usage - can be called from a Spring Boot CommandLineRunner or REST endpoint
     */
    public void processExample(String s3Key) {
        try {
            List<Object[]> data = readCsvFromS3(s3Key);
            System.out.println("Total rows processed: " + data.size());
        } catch (IOException e) {
            System.err.println("Error processing CSV: " + e.getMessage());
        }
    }
}
