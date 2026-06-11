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
 * CSV processing utility that reads CSV files from Amazon S3 instead of the local file system.
 * Replaces java.io.File-based operations with AWS SDK S3 client calls to achieve
 * cloud-native, durable, and scalable storage without host-level file system dependencies.
 */
public class CSVTest {

    public static void main(String[] args) {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException("Environment variable AWS_S3_BUCKET_NAME is not set.");
        }
        // S3 key for the CSV file (can be passed as argument or environment variable)
        String s3Key = (args.length > 0) ? args[0] : System.getenv("AWS_S3_CSV_KEY");
        if (s3Key == null || s3Key.isEmpty()) {
            throw new IllegalStateException(
                    "S3 object key must be provided as a program argument or via AWS_S3_CSV_KEY environment variable.");
        }

        S3Client s3Client = S3Client.builder().build();

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            System.out.println("Successfully retrieved S3 object: " + s3Key + " from bucket: " + bucketName);

            reader = new CSVReader(new InputStreamReader(s3Object, StandardCharsets.UTF_8));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (S3Exception e) {
            System.err.println("Failed to read CSV from S3: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }

}
