package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV processing utility that reads CSV data from Amazon S3 instead of the
 * local file system.  Replaces the previous java.io.File / JFileChooser approach
 * with cloud-native S3 object storage to eliminate host file-system dependencies.
 */
public class CSVTest {

    public static void main(String[] args) {
        // S3 bucket and key are resolved from environment variables so that no
        // hard-coded paths or host file-system references remain in the code.
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String objectKey  = System.getenv("AWS_S3_CSV_OBJECT_KEY");

        if (bucketName == null || objectKey == null) {
            System.err.println("Environment variables AWS_S3_BUCKET_NAME and "
                    + "AWS_S3_CSV_OBJECT_KEY must be set.");
            return;
        }

        // Build an S3 client using the default credential/region provider chain
        // (IAM role, environment variables, ~/.aws/credentials, etc.)
        S3Client s3Client = S3Client.builder().build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        List<Object[]> data = new ArrayList<>();
        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
             CSVReader reader = new CSVReader(
                     new InputStreamReader(s3Object, StandardCharsets.UTF_8))) {

            System.out.println("Reading CSV from S3: s3://" + bucketName + "/" + objectKey);
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 2 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
