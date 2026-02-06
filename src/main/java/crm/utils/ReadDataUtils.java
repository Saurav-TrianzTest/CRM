package crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadDataUtils {

    private static final Logger log = LoggerFactory.getLogger(ReadDataUtils.class);

    /**
     * Cloud-compatible file reading method using MultipartFile upload
     * Replaces Desktop GUI file chooser with web-based file upload
     */
    public static File readFileFromUpload(MultipartFile uploadedFile) throws IOException {
        if (uploadedFile == null || uploadedFile.isEmpty()) {
            log.warn("No file uploaded");
            return null;
        }

        String originalFilename = uploadedFile.getOriginalFilename();
        log.info("Processing uploaded file: {}", originalFilename);

        // Use temporary directory for cloud compatibility
        String tempDir = System.getProperty("java.io.tmpdir");
        Path tempFile = Paths.get(tempDir, originalFilename);

        uploadedFile.transferTo(tempFile.toFile());
        log.info("File saved to temporary location: {}", tempFile);

        return tempFile.toFile();
    }

    /**
     * Validates file extension for uploaded files
     */
    public static boolean validateFileExtension(String filename, String... allowedExtensions) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        String lowerFilename = filename.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowerFilename.endsWith("." + ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @deprecated This method uses Desktop GUI components and is not cloud-compatible.
     * Use readFileFromUpload(MultipartFile) instead for cloud deployments.
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        log.error("Desktop GUI file chooser is not supported in cloud environments. Use web-based file upload instead.");
        throw new UnsupportedOperationException("Desktop GUI components are not supported in cloud environments. Use MultipartFile upload instead.");
    }

}
