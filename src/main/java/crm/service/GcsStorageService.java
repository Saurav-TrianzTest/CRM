package crm.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Service for Google Cloud Storage operations.
 * Provides cloud-native file storage capabilities replacing local file system dependencies.
 */
@Service
@Slf4j
public class GcsStorageService {

    private final Storage storage;
    private final String bucketName;

    @Autowired
    public GcsStorageService(Storage storage, @Value("${gcs.bucket.name:crm-data-bucket}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    /**
     * Uploads a file to Google Cloud Storage.
     * 
     * @param blobName The name/path of the file in GCS
     * @param content The file content as byte array
     * @param contentType The MIME type of the file
     * @return The blob name where the file was stored
     */
    public String uploadFile(String blobName, byte[] content, String contentType) {
        try {
            BlobId blobId = BlobId.of(bucketName, blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();
            
            storage.create(blobInfo, content);
            log.info("File uploaded to GCS: {}", blobName);
            return blobName;
        } catch (Exception e) {
            log.error("Error uploading file to GCS: {}", blobName, e);
            throw new RuntimeException("Failed to upload file to GCS", e);
        }
    }

    /**
     * Downloads a file from Google Cloud Storage.
     * 
     * @param blobName The name/path of the file in GCS
     * @return The file content as byte array
     */
    public byte[] downloadFile(String blobName) {
        try {
            Blob blob = storage.get(bucketName, blobName);
            if (blob == null) {
                throw new RuntimeException("File not found in GCS: " + blobName);
            }
            log.info("File downloaded from GCS: {}", blobName);
            return blob.getContent();
        } catch (Exception e) {
            log.error("Error downloading file from GCS: {}", blobName, e);
            throw new RuntimeException("Failed to download file from GCS", e);
        }
    }

    /**
     * Gets an InputStream for a file in Google Cloud Storage.
     * 
     * @param blobName The name/path of the file in GCS
     * @return InputStream of the file content
     */
    public InputStream getFileInputStream(String blobName) {
        byte[] content = downloadFile(blobName);
        return new ByteArrayInputStream(content);
    }

    /**
     * Downloads a file from GCS to a temporary location.
     * 
     * @param blobName The name/path of the file in GCS
     * @return Path to the temporary file
     */
    public Path downloadToTempFile(String blobName) throws IOException {
        try {
            byte[] content = downloadFile(blobName);
            String extension = getFileExtension(blobName);
            Path tempFile = Files.createTempFile("gcs-", extension);
            
            try (InputStream inputStream = new ByteArrayInputStream(content)) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            log.info("File downloaded to temp location: {}", tempFile);
            return tempFile;
        } catch (Exception e) {
            log.error("Error downloading file to temp location: {}", blobName, e);
            throw new IOException("Failed to download file to temp location", e);
        }
    }

    /**
     * Deletes a file from Google Cloud Storage.
     * 
     * @param blobName The name/path of the file in GCS
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteFile(String blobName) {
        try {
            boolean deleted = storage.delete(bucketName, blobName);
            if (deleted) {
                log.info("File deleted from GCS: {}", blobName);
            } else {
                log.warn("File not found for deletion in GCS: {}", blobName);
            }
            return deleted;
        } catch (Exception e) {
            log.error("Error deleting file from GCS: {}", blobName, e);
            throw new RuntimeException("Failed to delete file from GCS", e);
        }
    }

    /**
     * Checks if a file exists in Google Cloud Storage.
     * 
     * @param blobName The name/path of the file in GCS
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String blobName) {
        try {
            Blob blob = storage.get(bucketName, blobName);
            return blob != null && blob.exists();
        } catch (Exception e) {
            log.error("Error checking file existence in GCS: {}", blobName, e);
            return false;
        }
    }

    /**
     * Lists all files in the bucket with a specific prefix.
     * 
     * @param prefix The prefix to filter files
     * @return Iterable of Blob objects
     */
    public Iterable<Blob> listFiles(String prefix) {
        try {
            return storage.list(bucketName, Storage.BlobListOption.prefix(prefix)).iterateAll();
        } catch (Exception e) {
            log.error("Error listing files from GCS with prefix: {}", prefix, e);
            throw new RuntimeException("Failed to list files from GCS", e);
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
}
