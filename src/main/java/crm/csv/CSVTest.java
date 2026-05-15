package crm.csv;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV processing utility that uses Amazon S3 for data storage
 * instead of local file system dependencies.
 */
public class CSVTest {

    private static final String DEFAULT_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
    private static final String DEFAULT_BUCKET = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-csv-storage");

    /**
     * Reads and processes CSV file from Amazon S3.
     * 
     * @param bucketName The S3 bucket name
     * @param s3Key The S3 object key (path)
     * @return List of CSV rows as Object arrays
     * @throws IOException if S3 read fails
     */
    public static List<Object[]> readCsvFromS3(String bucketName, String s3Key) throws IOException {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(DEFAULT_REGION)
                .build();
        
        S3Object s3Object = s3Client.getObject(bucketName, s3Key);
        S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
        
        CSVReader reader = new CSVReader(new InputStreamReader(s3InputStream));
        List<Object[]> data = new ArrayList<>();
        
        try {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                // Process specific rows
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
        } finally {
            reader.close();
            s3InputStream.close();
        }
        
        return data;
    }
    
    /**
     * Reads CSV from classpath resources (for bundled configuration files).
     * 
     * @param resourcePath The classpath resource path
     * @return List of CSV rows as Object arrays
     * @throws IOException if resource read fails
     */
    public static List<Object[]> readCsvFromClasspath(String resourcePath) throws IOException {
        CSVReader reader = new CSVReader(
            new InputStreamReader(
                CSVTest.class.getClassLoader().getResourceAsStream(resourcePath)
            )
        );
        
        List<Object[]> data = new ArrayList<>();
        
        try {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                // Process specific rows
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
        } finally {
            reader.close();
        }
        
        return data;
    }

    /**
     * Example main method demonstrating cloud-native CSV processing.
     * In production, CSV file location should be provided via environment variables
     * or configuration management (AWS Systems Manager Parameter Store).
     */
    public static void main(String[] args) {
        try {
            // Example 1: Read from S3 (production approach)
            String s3Bucket = System.getenv().getOrDefault("CSV_BUCKET_NAME", DEFAULT_BUCKET);
            String s3Key = System.getenv().getOrDefault("CSV_FILE_KEY", "data/sample.csv");
            
            System.out.println("Reading CSV from S3: bucket=" + s3Bucket + ", key=" + s3Key);
            List<Object[]> data = readCsvFromS3(s3Bucket, s3Key);
            System.out.println("Successfully read " + data.size() + " rows from S3");
            
            // Example 2: Read from classpath (for bundled resources)
            // List<Object[]> data = readCsvFromClasspath("data/sample.csv");
            
        } catch (IOException e) {
            System.err.println("Failed to read CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
