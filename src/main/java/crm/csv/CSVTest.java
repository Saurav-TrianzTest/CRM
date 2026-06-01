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
 * CSV processing utility that reads CSV files from Amazon S3 instead of local file system.
 * Replaces java.io.File-based operations with cloud-native S3 object storage access.
 */
public class CSVTest {

    public static void main(String[] args) {
        // Retrieve S3 configuration from environment variables
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = System.getProperty("aws.s3.bucketName", "crm-default-bucket");
        }
        // S3 key for the CSV file (can be overridden via environment variable or argument)
        String s3Key = (args.length > 0) ? args[0] : System.getenv("CSV_S3_KEY");
        if (s3Key == null || s3Key.isEmpty()) {
            s3Key = "csv/input.csv";
        }

        // Build S3 client using the default credential provider chain (IAM role / env vars)
        S3Client s3Client = S3Client.builder().build();

        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved CSV from S3: s3://" + bucketName + "/" + s3Key);

            // Read CSV content directly from the S3 InputStream — no local file needed
            reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (S3Exception e) {
            System.err.println("Failed to read CSV from S3: " + e.awsErrorDetails().errorMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            s3Client.close();
        }
    }

}
