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
 * CSVTest - Cloud-native version using Azure Blob Storage.
 *
 * Instead of reading a CSV from the local file system via java.io.File,
 * this class downloads the CSV blob from Azure Blob Storage and processes
 * it in-memory, ensuring data durability, scalability, and cloud-native
 * compliance on Azure.
 *
 * Required environment variables:
 *   AZURE_STORAGE_CONNECTION_STRING  - Azure Storage account connection string
 *   AZURE_BLOB_CONTAINER_NAME        - Name of the blob container holding CSV files
 *   AZURE_BLOB_CSV_NAME              - Name of the CSV blob to process
 */
public class CSVTest {

    public static void main(String[] args) {
        // Retrieve Azure Blob Storage configuration from environment variables
        String connectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
        String containerName   = System.getenv("AZURE_BLOB_CONTAINER_NAME");
        String blobName        = System.getenv("AZURE_BLOB_CSV_NAME");

        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalStateException(
                "Environment variable AZURE_STORAGE_CONNECTION_STRING is not set.");
        }
        if (containerName == null || containerName.isEmpty()) {
            throw new IllegalStateException(
                "Environment variable AZURE_BLOB_CONTAINER_NAME is not set.");
        }
        if (blobName == null || blobName.isEmpty()) {
            throw new IllegalStateException(
                "Environment variable AZURE_BLOB_CSV_NAME is not set.");
        }

        // Build Azure Blob Storage client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // Download the CSV blob as an InputStream and process it in-memory
        List<Object[]> data = new ArrayList<>();
        try (InputStream blobInputStream = blobClient.openInputStream();
             CSVReader reader = new CSVReader(new InputStreamReader(blobInputStream))) {

            String[] line;
            while ((line = reader.readNext()) != null) {
//                System.out.println(line[1] + "\t" + line[2]);
                data.add(line);
                if (line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*System.out.println(data.get(0)[1] + "\t" + data.get(0)[2]);
        System.out.println(data.get(1)[1] + "\t" + data.get(1)[2]);*/
    }

}
