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
 * Cloud-native CSV processing using Amazon S3 for data storage.
 * Replaces local file system dependencies with S3 object storage.
 */
public class CSVTest {

    public static void main(String[] args) {
        // Configuration from environment variables for cloud deployment
        String bucketName = System.getenv().getOrDefault("AWS_S3_BUCKET_NAME", "default-csv-bucket");
        String s3Key = System.getenv().getOrDefault("CSV_S3_KEY", "data/sample.csv");
        String awsRegion = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

        // Initialize S3 client with default credentials provider (uses IAM roles in cloud)
        S3Client s3Client = S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        try {
            // Read CSV file from S3 instead of local file system
            ResponseInputStream<GetObjectResponse> s3Object = readCsvFromS3(s3Client, bucketName, s3Key);
            
            CSVReader reader = new CSVReader(new InputStreamReader(s3Object));
            List<Object[]> data = new ArrayList<>();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            reader.close();
            s3Object.close();
            
            System.out.println("Successfully processed CSV from S3: s3://" + bucketName + "/" + s3Key);
            
        } catch (IOException e) {
            System.err.println("Error processing CSV file from S3: " + e.getMessage());
            e.printStackTrace();
        } catch (S3Exception e) {
            System.err.println("S3 error: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }

    /**
     * Reads CSV file from Amazon S3.
     * 
     * @param s3Client AWS S3 client
     * @param bucketName S3 bucket name
     * @param s3Key S3 object key
     * @return InputStream of the S3 object
     * @throws S3Exception if the object cannot be retrieved
     */
    private static ResponseInputStream<GetObjectResponse> readCsvFromS3(S3Client s3Client, String bucketName, String s3Key) throws S3Exception {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        return s3Client.getObject(getObjectRequest);
    }
}
