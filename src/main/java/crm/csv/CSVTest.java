package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV processing utility.
 * Reads CSV data directly from Amazon S3 instead of the local file system,
 * eliminating java.io.File dependencies and ensuring cloud-native, durable storage access.
 */
public class CSVTest {

    public static void main(String[] args) {
        // Resolve S3 configuration from environment variables
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String objectKey  = System.getenv("AWS_S3_CSV_OBJECT_KEY");

        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_BUCKET_NAME is not set.");
        }
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_CSV_OBJECT_KEY is not set.");
        }

        // Build a default S3Client (credentials / region resolved from the environment)
        S3Client s3Client = S3Client.create();

        // Retrieve the CSV file as an InputStream from S3 (replaces java.io.File / FileReader)
        InputStream csvStream = ReadDataUtils.readFileFromS3(bucketName, objectKey, s3Client);
        if (csvStream == null) {
            System.err.println("Could not retrieve CSV from S3. Aborting.");
            return;
        }

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }
}
