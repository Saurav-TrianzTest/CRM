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
 * Cloud-ready CSV Test utility that reads CSV files from Google Cloud Storage
 * instead of local file system.
 */
public class CSVTest {

    private static final String BUCKET_NAME = System.getenv().getOrDefault("GCS_BUCKET_NAME", "crm-data-bucket");
    private static final String CSV_FOLDER = "csv/";

    public static void main(String[] args) {
        // Example: Read CSV file from GCS
        // Replace "sample.csv" with actual file name in GCS bucket
        String csvFileName = System.getenv().getOrDefault("CSV_FILE_NAME", "sample.csv");
        String blobName = CSV_FOLDER + csvFileName;
        
        try {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            Blob blob = storage.get(BUCKET_NAME, blobName);
            
            if (blob == null) {
                System.err.println("CSV file not found in GCS: " + blobName);
                System.err.println("Please ensure the file exists in bucket: " + BUCKET_NAME);
                return;
            }
            
            byte[] content = blob.getContent();
            System.out.println("Successfully retrieved CSV file from GCS: " + blobName);
            
            // Read CSV content from GCS
            CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(content)));
            List<Object[]> data = new ArrayList<>();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            reader.close();
            System.out.println("Total rows processed: " + data.size());
            
        } catch (IOException e) {
            System.err.println("Error reading CSV file from GCS: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error accessing Google Cloud Storage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Utility method to read CSV from GCS and return as list of string arrays.
     * 
     * @param blobName The name/path of the CSV file in GCS bucket
     * @return List of string arrays representing CSV rows
     */
    public static List<String[]> readCsvFromGCS(String blobName) throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.get(BUCKET_NAME, blobName);
        
        if (blob == null) {
            throw new IOException("CSV file not found in GCS: " + blobName);
        }
        
        byte[] content = blob.getContent();
        List<String[]> data = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(content)))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        }
        
        return data;
    }
}
