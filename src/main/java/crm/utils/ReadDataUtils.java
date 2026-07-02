package crm.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Cloud-ready utility for reading files from Google Cloud Storage.
 * Replaces hard-coded file paths with GCS bucket operations.
 */
@Component
public class ReadDataUtils {

    @Value("${gcs.bucket.name:default-crm-bucket}")
    private String bucketName;

    @Value("${gcs.project.id:#{null}}")
    private String projectId;

    /**
     * Reads a file from Google Cloud Storage and returns it as an InputStream.
     * 
     * @param blobName The name/path of the file in GCS bucket
     * @return InputStream of the file content
     */
    public InputStream readFileFromGCS(String blobName) {
        try {
            Storage storage = getStorageClient();
            Blob blob = storage.get(bucketName, blobName);
            
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
     * Downloads a file from GCS to a temporary local location for processing.
     * Use this when legacy code requires a File object.
     * 
     * @param blobName The name/path of the file in GCS bucket
     * @return Path to the temporary file
     */
    public Path downloadFileFromGCS(String blobName) {
        try {
            Storage storage = getStorageClient();
            Blob blob = storage.get(bucketName, blobName);
            
            if (blob == null) {
                throw new RuntimeException("File not found in GCS: " + blobName);
            }
            
            // Create temporary file
            Path tempFile = Files.createTempFile("gcs-download-", getFileExtension(blobName));
            
            // Download to temp file
            blob.downloadTo(tempFile);
            
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from GCS: " + blobName, e);
        }
    }

    /**
     * Lists all files in the GCS bucket with a given prefix.
     * 
     * @param prefix The prefix to filter files (e.g., "csv/", "pdf/")
     * @return Iterable of blob names
     */
    public Iterable<Blob> listFilesInGCS(String prefix) {
        try {
            Storage storage = getStorageClient();
            return storage.list(bucketName, Storage.BlobListOption.prefix(prefix)).iterateAll();
        } catch (Exception e) {
            throw new RuntimeException("Error listing files from GCS with prefix: " + prefix, e);
        }
    }

    private Storage getStorageClient() {
        if (projectId != null && !projectId.isEmpty()) {
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .build()
                    .getService();
        } else {
            // Use default credentials and project
            return StorageOptions.getDefaultInstance().getService();
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
}
