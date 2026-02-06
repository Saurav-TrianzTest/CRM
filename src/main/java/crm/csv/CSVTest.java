package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVTest {

    public static void main(String[] args) {
        // Cloud-ready CSV reading - use environment variables for configuration
        String csvSource = System.getenv().getOrDefault("CSV_SOURCE", "classpath");
        String csvPath = System.getenv().getOrDefault("CSV_FILE_PATH", "data/sample.csv");
        String s3Bucket = System.getenv().getOrDefault("CSV_S3_BUCKET", "csv-data-bucket");
        String s3Key = System.getenv().getOrDefault("CSV_S3_KEY", "uploads/data.csv");

        File document = null;
        try {
            if ("s3".equalsIgnoreCase(csvSource)) {
                log.info("Reading CSV from S3: s3://{}/{}", s3Bucket, s3Key);
                S3Client s3Client = S3Client.builder().build();
                document = ReadDataUtils.readFileFromS3(s3Client, s3Bucket, s3Key);
            } else {
                log.info("Reading CSV from classpath: {}", csvPath);
                document = ReadDataUtils.readFileFromClasspath(csvPath);
            }
        } catch (IOException e) {
            log.error("Failed to read CSV file", e);
            System.exit(1);
        }

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(document));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if(line.length > 1 && line[1].equals("QUICK SUB")){
                    log.info("Found matching row: {} | {} | {}", line[0], line[1], line[2]);
                }
            }
            reader.close();
            log.info("Successfully processed {} rows from CSV", data.size());
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
        } finally {
            // Clean up temporary file if it was created
            if (document != null && document.exists()) {
                document.delete();
            }
        }
    }

}
