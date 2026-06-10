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
 * CSVTest reads CSV data from Amazon S3 instead of the local file system.
 * Replaces java.io.File-based operations with AWS SDK for Java v2 S3 client calls
 * to achieve cloud-native, durable, and scalable storage without host-level
 * file system dependencies.
 */
public class CSVTest {

    public static void main(String[] args) {
        // S3 bucket and key are resolved from environment variables for cloud portability
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME") != null
                ? System.getenv("AWS_S3_BUCKET_NAME")
                : "crm-csv-storage";
        String s3Key = System.getenv("CSV_S3_KEY") != null
                ? System.getenv("CSV_S3_KEY")
                : "data/input.csv";

        // Build S3 client using the AWS default credential chain
        // (environment variables, IAM role, or instance profile in cloud environments)
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

            // Read CSV content directly from the S3 InputStream — no local file needed
            reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            while ((line = reader.readNext()) != null) {
//                System.out.println(line[1] + "\t" + line[2]);
                data.add(line);
                if (line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (S3Exception e) {
            System.err.println("Failed to read CSV from S3: " + e.awsErrorDetails().errorMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*System.out.println(data.get(0)[1] + "\t" + data.get(0)[2]);
        System.out.println(data.get(1)[1] + "\t" + data.get(1)[2]);*/
    }

}
