package crm.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Cloud-ready utility for reading data from Google Cloud Storage.
 * Replaces hard-coded file paths with GCS bucket operations.
 */
@Component
public class ReadDataUtils {

    private static final String BUCKET_NAME = System.getenv().getOrDefault("GCS_BUCKET_NAME", "crm-data-bucket");
    private final Storage storage;

    public ReadDataUtils() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    /**
     * Reads a file from Google Cloud Storage and returns it as an InputStream.
     * 
     * @param blobName The name/path of the file in GCS bucket
     * @return InputStream of the file content
     */
    public InputStream readFileFromGCS(String blobName) {
        try {
            Blob blob = storage.get(BUCKET_NAME, blobName);
            if (blob == null) {
                throw new RuntimeException("File not found in GCS: " + blobName);
            }
            byte[] content = blob.getContent();
            return new ByteArrayInputStream(content);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file from GCS: " + blobName, e);
        }
    }

    /**
     * Downloads a file from GCS to a temporary location for processing.
     * 
     * @param blobName The name/path of the file in GCS bucket
     * @return Path to the temporary file
     */
    public Path downloadFileFromGCS(String blobName) {
        try {
            Blob blob = storage.get(BUCKET_NAME, blobName);
            if (blob == null) {
                throw new RuntimeException("File not found in GCS: " + blobName);
            }
            
            // Create temporary file
            Path tempFile = Files.createTempFile("gcs-download-", getFileExtension(blobName));
            
            // Download content to temporary file
            try (InputStream inputStream = new ByteArrayInputStream(blob.getContent())) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from GCS: " + blobName, e);
        }
    }

    /**
     * Lists all files in the GCS bucket with a specific prefix.
     * 
     * @param prefix The prefix to filter files (e.g., "csv/", "data/")
     * @return Iterable of Blob objects
     */
    public Iterable<Blob> listFilesInGCS(String prefix) {
        try {
            return storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(prefix)).iterateAll();
        } catch (Exception e) {
            throw new RuntimeException("Error listing files from GCS with prefix: " + prefix, e);
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
}
