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
 * Cloud-ready CSV Test utility that uses Azure Blob Storage.
 * Replaces java.io.File operations with Azure Blob Storage operations.
 */
public class CSVTest {

    private static final String CONNECTION_STRING = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
    private static final String CONTAINER_NAME = System.getenv("AZURE_STORAGE_CONTAINER_NAME") != null 
            ? System.getenv("AZURE_STORAGE_CONTAINER_NAME") 
            : "crm-files";

    /**
     * Reads a CSV file from Azure Blob Storage.
     * 
     * @param blobName The name of the CSV blob to read
     * @return List of data rows
     */
    public static List<Object[]> readCsvFromAzureBlob(String blobName) {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set AZURE_STORAGE_CONNECTION_STRING environment variable.");
        }

        List<Object[]> data = new ArrayList<>();

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            
            if (!containerClient.exists()) {
                throw new IllegalStateException("Container does not exist: " + CONTAINER_NAME);
            }

            BlobClient blobClient = containerClient.getBlobClient(blobName);
            
            if (!blobClient.exists()) {
                throw new IllegalArgumentException("Blob not found: " + blobName);
            }

            // Read CSV from Azure Blob Storage
            try (InputStream inputStream = blobClient.openInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 CSVReader reader = new CSVReader(inputStreamReader)) {
                
                String[] line;
                while ((line = reader.readNext()) != null) {
                    data.add(line);
                    if (line.length > 1 && line[1].equals("QUICK SUB")) {
                        System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Lists all CSV files in the Azure Blob Storage container.
     * 
     * @return List of CSV blob names
     */
    public static List<String> listCsvFiles() {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured.");
        }

        List<String> csvFiles = new ArrayList<>();

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            
            if (containerClient.exists()) {
                containerClient.listBlobs().forEach(blobItem -> {
                    if (blobItem.getName().endsWith(".csv")) {
                        csvFiles.add(blobItem.getName());
                    }
                });
            }

        } catch (Exception e) {
            System.err.println("Error listing CSV files from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
        }

        return csvFiles;
    }

    public static void main(String[] args) {
        // Example usage: Read a specific CSV file from Azure Blob Storage
        // Replace "your-file.csv" with the actual blob name
        String blobName = args.length > 0 ? args[0] : "sample.csv";
        
        System.out.println("Reading CSV file from Azure Blob Storage: " + blobName);
        
        try {
            List<Object[]> data = readCsvFromAzureBlob(blobName);
            System.out.println("Successfully read " + data.size() + " rows from Azure Blob Storage");
            
            // List all available CSV files
            System.out.println("\nAvailable CSV files in Azure Blob Storage:");
            List<String> csvFiles = listCsvFiles();
            csvFiles.forEach(file -> System.out.println("  - " + file));
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nUsage: java crm.csv.CSVTest <blob-name>");
            System.err.println("Make sure to set AZURE_STORAGE_CONNECTION_STRING environment variable");
        }
    }
}
