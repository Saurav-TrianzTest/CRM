package crm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Cloud-native file reading utility
 * Replaced Swing JFileChooser with classpath resource loading for cloud compatibility
 * This utility now loads files from classpath or accepts file paths programmatically
 */
@Slf4j
public class ReadDataUtils {

    /**
     * Reads file from classpath resources (cloud-compatible)
     * Replaced Swing-based file chooser which is incompatible with headless cloud environments
     *
     * @param resourcePath Path to resource in classpath (e.g., "data/file.csv")
     * @return InputStream of the resource
     * @throws IOException if resource not found
     */
    public static InputStream readFileFromClasspath(String resourcePath) throws IOException {
        log.info("Loading resource from classpath: {}", resourcePath);
        Resource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            log.error("Resource not found in classpath: {}", resourcePath);
            throw new IOException("Resource not found: " + resourcePath);
        }

        return resource.getInputStream();
    }

    /**
     * Reads file from absolute file path (use with caution in cloud)
     * For cloud deployments, consider using S3 or other cloud storage
     *
     * @param filePath Absolute path to file
     * @return File object
     * @throws IOException if file not found
     */
    public static File readFileFromPath(String filePath) throws IOException {
        log.info("Reading file from path: {}", filePath);
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            log.error("File not found at path: {}", filePath);
            throw new IOException("File not found: " + filePath);
        }

        return path.toFile();
    }

    /**
     * Validates file extension
     *
     * @param fileName File name to validate
     * @param allowedExtensions Allowed extensions (e.g., "csv", "xlsx")
     * @return true if extension is allowed
     */
    public static boolean validateFileExtension(String fileName, String... allowedExtensions) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        String extension = "";
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = fileName.substring(lastDotIndex + 1).toLowerCase();
        }

        for (String allowed : allowedExtensions) {
            if (extension.equals(allowed.toLowerCase())) {
                log.info("File {} has valid extension: {}", fileName, extension);
                return true;
            }
        }

        log.warn("File {} has invalid extension. Allowed: {}", fileName, String.join(", ", allowedExtensions));
        return false;
    }

}
