package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-native CSV processing utility
 * Updated to use classpath resources or S3 instead of local file system
 */
public class CSVTest {

    private static final Logger log = LoggerFactory.getLogger(CSVTest.class);

    public static void main(String[] args) {
        // For cloud deployment, read from classpath resources or environment variable
        String csvResourcePath = System.getenv("CSV_FILE_PATH");
        if (csvResourcePath == null || csvResourcePath.isEmpty()) {
            csvResourcePath = "sample-data.csv"; // Default classpath resource
            log.info("No CSV_FILE_PATH environment variable set, using default: {}", csvResourcePath);
        }

        List<Object[]> data = new ArrayList<>();
        try (InputStream inputStream = ReadDataUtils.readFromClasspath(csvResourcePath);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    log.info("Found QUICK SUB record: {} | {} | {}",
                            line[0], line[1], line.length > 2 ? line[2] : "N/A");
                }
            }
            log.info("CSV processing completed. Total records: {}", data.size());
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
        } catch (IllegalArgumentException e) {
            log.error("CSV file not found in classpath: {}", csvResourcePath, e);
        }
    }

}
