package crm.utils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Utility class for reading data from Azure Blob Storage.
 * Replaces the previous hard-coded file path / JFileChooser-based local file access
 * with Azure Blob Storage operations to ensure cloud-native compatibility.
 */
public class ReadDataUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReadDataUtils.class);

    /**
     * Reads a blob from Azure Blob Storage and returns its content as an InputStream.
     *
     * The connection string and container name are resolved from environment variables:
     *   AZURE_STORAGE_CONNECTION_STRING  – Azure Storage account connection string
     *   AZURE_BLOB_CONTAINER_NAME        – target blob container name
     *
     * @param blobName name of the blob to download (e.g. "data.csv")
     * @return InputStream of the blob content, or null if the blob does not exist
     */
    public static InputStream readBlobAsStream(String blobName) {
        String connectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
        String containerName   = System.getenv("AZURE_BLOB_CONTAINER_NAME");

        if (connectionString == null || connectionString.isEmpty()) {
            logger.error("Environment variable AZURE_STORAGE_CONNECTION_STRING is not set.");
            return null;
        }
        if (containerName == null || containerName.isEmpty()) {
            logger.error("Environment variable AZURE_BLOB_CONTAINER_NAME is not set.");
            return null;
        }

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            if (!blobClient.exists()) {
                logger.warn("Blob '{}' does not exist in container '{}'.", blobName, containerName);
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.downloadStream(outputStream);
            logger.info("Successfully downloaded blob '{}' from container '{}'.", blobName, containerName);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (Exception e) {
            logger.error("Failed to read blob '{}' from Azure Blob Storage: {}", blobName, e.getMessage(), e);
            return null;
        }
    }

}
