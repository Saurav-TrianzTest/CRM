package crm.csv;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVTest {

    private static final Logger log = LoggerFactory.getLogger(CSVTest.class);

    public static void main(String[] args) {
        // Cloud-compatible CSV reading from environment variable or classpath
        String csvFileName = System.getenv("CSV_FILE_NAME");
        if (csvFileName == null || csvFileName.isEmpty()) {
            csvFileName = "data.csv"; // default file name
        }

        List<Object[]> data = processCSVFile(csvFileName);
        log.info("CSV processing completed. Total records: {}", data.size());
    }

    public static List<Object[]> processCSVFile(String fileName) {
        List<Object[]> data = new ArrayList<>();
        CSVReader reader = null;

        try {
            // Try reading from classpath (cloud-compatible)
            InputStream is = CSVTest.class.getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                // Try reading from configured storage path
                String storagePath = System.getenv("FILE_STORAGE_PATH");
                if (storagePath != null) {
                    java.nio.file.Path filePath = java.nio.file.Paths.get(storagePath, fileName);
                    is = java.nio.file.Files.newInputStream(filePath);
                }
            }

            if (is != null) {
                reader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String[] line;
                while ((line = reader.readNext()) != null) {
                    data.add(line);
                    if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                        log.info("Found QUICK SUB record: {} | {} | {}",
                                line.length > 0 ? line[0] : "",
                                line.length > 1 ? line[1] : "",
                                line.length > 2 ? line[2] : "");
                    }
                }
                log.info("Successfully processed CSV file: {}", fileName);
            } else {
                log.error("CSV file not found: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Error processing CSV file: {}", e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Error closing CSV reader: {}", e.getMessage());
                }
            }
        }

        return data;
    }

}
