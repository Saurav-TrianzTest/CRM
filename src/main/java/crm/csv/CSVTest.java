package crm.csv;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * CSV processing utility.
 * Replaces the previous {@code java.io.File} / JFileChooser-based approach
 * with Amazon S3 object storage access using AWS SDK for Java v2, eliminating
 * host-level file system dependencies for cloud-native, durable storage.
 */
public class CSVTest {

    private static final Logger logger = LoggerFactory.getLogger(CSVTest.class);

    /**
     * Reads a CSV file directly from Amazon S3 and processes its contents.
     * Replaces the previous {@code new FileReader(document)} local file read
     * with an S3 {@link ResponseInputStream} wrapped in an {@link InputStreamReader}.
     *
     * @param s3Client   an initialised AWS SDK v2 S3Client
     * @param bucketName the S3 bucket name (supplied via {@code AWS_S3_BUCKET_NAME})
     * @param objectKey  the S3 object key for the CSV file
     * @return list of parsed CSV rows
     */
    public static List<Object[]> readCsvFromS3(S3Client s3Client, String bucketName, String objectKey) {
        List<Object[]> data = new ArrayList<>();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
             CSVReader reader = new CSVReader(new InputStreamReader(s3Object, StandardCharsets.UTF_8))) {

            logger.info("Reading CSV from S3: s3://{}/{}", bucketName, objectKey);
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    logger.info("{}\t{}\t{}", line[0], line[1], line.length > 2 ? line[2] : "");
                }
            }
            logger.info("Successfully read {} rows from S3 CSV: s3://{}/{}", data.size(), bucketName, objectKey);

        } catch (IOException e) {
            logger.error("Failed to read CSV from S3: s3://{}/{} - {}", bucketName, objectKey, e.getMessage());
        }

        return data;
    }

    /**
     * Entry point retained for backward compatibility.
     * In a cloud environment, invoke {@link #readCsvFromS3(S3Client, String, String)}
     * directly, supplying an S3Client configured via the default AWS credential chain
     * and the bucket/key from environment variables {@code AWS_S3_BUCKET_NAME} and
     * {@code AWS_S3_CSV_OBJECT_KEY}.
     */
    public static void main(String[] args) {
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        String objectKey  = System.getenv("AWS_S3_CSV_OBJECT_KEY");

        if (bucketName == null || objectKey == null) {
            logger.error("Environment variables AWS_S3_BUCKET_NAME and AWS_S3_CSV_OBJECT_KEY must be set.");
            return;
        }

        S3Client s3Client = S3Client.builder().build();
        List<Object[]> data = readCsvFromS3(s3Client, bucketName, objectKey);
        logger.info("Total rows processed: {}", data.size());
    }
}
