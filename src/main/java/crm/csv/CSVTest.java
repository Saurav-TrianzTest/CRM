package crm.csv;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV Test utility that reads CSV files from Azure Blob Storage.
 * Replaces java.io.File operations with Azure Blob Storage operations.
 */
public class CSVTest {

    private static final String CONNECTION_STRING = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
    private static final String CONTAINER_NAME = System.getenv().getOrDefault("AZURE_STORAGE_CONTAINER_NAME", "crm-files");

    /**
     * Reads a CSV file from Azure Blob Storage and processes it.
     * 
     * @param blobName The name of the CSV blob to read
     */
    public static void readCsvFromAzureBlob(String blobName) {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            System.err.println("ERROR: AZURE_STORAGE_CONNECTION_STRING environment variable is not set");
            return;
        }

        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Connect to Azure Blob Storage
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            if (!blobClient.exists()) {
                System.err.println("ERROR: Blob does not exist: " + blobName);
                return;
            }

            // Read CSV from Azure Blob Storage
            InputStream inputStream = blobClient.openInputStream();
            reader = new CSVReader(new InputStreamReader(inputStream));
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            System.out.println("Successfully processed " + data.size() + " rows from Azure Blob Storage");
            
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read CSV from Azure Blob Storage");
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

    /**
     * Lists all CSV files in the Azure Blob Storage container.
     */
    public static void listCsvFilesInAzureBlob() {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            System.err.println("ERROR: AZURE_STORAGE_CONNECTION_STRING environment variable is not set");
            return;
        }

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            
            System.out.println("Available CSV files in Azure Blob Storage:");
            containerClient.listBlobs().forEach(blobItem -> {
                if (blobItem.getName().endsWith(".csv")) {
                    System.out.println("  - " + blobItem.getName());
                }
            });
        } catch (Exception e) {
            System.err.println("ERROR: Failed to list CSV files from Azure Blob Storage");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage: Read a specific CSV file from Azure Blob Storage
        // Replace "sample.csv" with the actual blob name
        String csvBlobName = args.length > 0 ? args[0] : "sample.csv";
        
        System.out.println("Reading CSV file from Azure Blob Storage: " + csvBlobName);
        readCsvFromAzureBlob(csvBlobName);
        
        // Optionally list all available CSV files
        System.out.println("\n");
        listCsvFilesInAzureBlob();
    }

}
