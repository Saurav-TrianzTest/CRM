package crm.csv;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
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
 * CSV processing utility.
 * Replaces java.io.File-based local file access (blocker-3) with Azure Blob Storage
 * to ensure data durability and cloud-native compliance.
 *
 * The blob name of the CSV file to process is supplied via the
 * AZURE_CSV_BLOB_NAME environment variable (defaults to "data.csv").
 */
public class CSVTest {

    private static final Logger logger = LoggerFactory.getLogger(CSVTest.class);

    public static void main(String[] args) {
        // Resolve the blob name from an environment variable instead of a hard-coded path
        String blobName = System.getenv("AZURE_CSV_BLOB_NAME");
        if (blobName == null || blobName.isEmpty()) {
            blobName = "data.csv";
        }

        // Download the CSV from Azure Blob Storage using ReadDataUtils
        InputStream csvStream = ReadDataUtils.readBlobAsStream(blobName);
        if (csvStream == null) {
            logger.error("Could not retrieve CSV blob '{}' from Azure Blob Storage.", blobName);
            return;
        }

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new InputStreamReader(csvStream));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            logger.info("CSV processing complete. Total rows read: {}", data.size());
        } catch (IOException e) {
            logger.error("Error reading CSV data from Azure Blob Storage stream: {}", e.getMessage(), e);
        }
    }

}
