package crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadDataUtils {

    private static final Logger log = LoggerFactory.getLogger(ReadDataUtils.class);

    /**
     * Cloud-compatible file reader that reads from classpath or configured storage path
     * @param fileName the name of the file to read
     * @param fileExtension expected file extension for validation
     * @return File object or null if not found
     */
    public static File ReadFile(String fileName, String... fileExtension) {
        // Try reading from environment-configured storage path
        String storagePath = System.getenv("FILE_STORAGE_PATH");
        if (storagePath != null && !storagePath.isEmpty()) {
            try {
                Path filePath = Paths.get(storagePath, fileName);
                if (Files.exists(filePath)) {
                    log.info("File found in storage path: {}", filePath);
                    return filePath.toFile();
                }
            } catch (Exception e) {
                log.error("Error reading file from storage path: {}", e.getMessage());
            }
        }

        // Try reading from classpath
        try {
            InputStream is = ReadDataUtils.class.getClassLoader().getResourceAsStream(fileName);
            if (is != null) {
                log.info("File found in classpath: {}", fileName);
                // For cloud environments, copy classpath resource to temp location
                Path tempFile = Files.createTempFile("cloud-file-", fileName);
                Files.copy(is, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                is.close();
                return tempFile.toFile();
            }
        } catch (Exception e) {
            log.error("Error reading file from classpath: {}", e.getMessage());
        }

        log.warn("File not found: {}", fileName);
        return null;
    }

    /**
     * Legacy method - deprecated for cloud environments
     * @deprecated Use ReadFile(String fileName, String... fileExtension) instead
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        log.warn("Legacy file chooser method called - not supported in cloud environments");
        return null;
    }

}
