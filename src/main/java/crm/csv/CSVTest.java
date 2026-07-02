package crm.csv;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.opencsv.CSVReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV test utility that reads from Google Cloud Storage
 * instead of local file system.
 */
public class CSVTest {

    private static final String BUCKET_NAME = System.getenv().getOrDefault("GCS_BUCKET_NAME", "default-crm-bucket");
    private static final String PROJECT_ID = System.getenv("GCS_PROJECT_ID");

    public static void main(String[] args) {
        // Example: Read CSV from GCS bucket
        // Usage: Specify the blob name (path) in GCS bucket
        String csvBlobName = System.getenv().getOrDefault("CSV_FILE_PATH", "csv/sample.csv");
        
        try {
            byte[] csvContent = readFileFromGCS(csvBlobName);
            processCSVData(csvContent);
        } catch (Exception e) {
            System.err.println("Error reading CSV from GCS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads file content from Google Cloud Storage.
     */
    private static byte[] readFileFromGCS(String blobName) {
        try {
            Storage storage = getStorageClient();
            Blob blob = storage.get(BUCKET_NAME, blobName);
            
            if (blob == null) {
                throw new RuntimeException("File not found in GCS: " + blobName);
            }
            
            System.out.println("Reading CSV from GCS: " + blobName);
            return blob.getContent();
        } catch (Exception e) {
            throw new RuntimeException("Error reading file from GCS: " + blobName, e);
        }
    }

    /**
     * Processes CSV data from byte array instead of File object.
     */
    private static void processCSVData(byte[] csvContent) {
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Read CSV from byte array instead of File
            ByteArrayInputStream bais = new ByteArrayInputStream(csvContent);
            reader = new CSVReader(new InputStreamReader(bais));
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            
            System.out.println("Total CSV rows processed: " + data.size());
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
        }
    }

    private static Storage getStorageClient() {
        if (PROJECT_ID != null && !PROJECT_ID.isEmpty()) {
            return StorageOptions.newBuilder()
                    .setProjectId(PROJECT_ID)
                    .build()
                    .getService();
        } else {
            return StorageOptions.getDefaultInstance().getService();
        }
    }
}
