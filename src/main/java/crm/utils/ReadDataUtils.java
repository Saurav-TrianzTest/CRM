package crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Cloud-native file utilities for handling file uploads.
 * Replaces Swing-based GUI file chooser with web-based file upload support.
 */
public class ReadDataUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReadDataUtils.class);

    /**
     * Process uploaded file from web interface
     * @param uploadedFile MultipartFile from web upload
     * @param allowedExtensions Allowed file extensions
     * @return File object if valid, null otherwise
     */
    public static File processUploadedFile(MultipartFile uploadedFile, String... allowedExtensions) {
        if (uploadedFile == null || uploadedFile.isEmpty()) {
            logger.warn("No file uploaded");
            return null;
        }

        String fileName = uploadedFile.getOriginalFilename();
        if (fileName == null) {
            logger.warn("File has no name");
            return null;
        }

        // Validate file extension
        boolean validExtension = false;
        for (String extension : allowedExtensions) {
            if (fileName.toLowerCase().endsWith("." + extension.toLowerCase())) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension) {
            logger.warn("Invalid file extension for file: {}", fileName);
            return null;
        }

        try {
            // Store temporarily for processing (cloud-native apps should use cloud storage)
            Path tempDir = Files.createTempDirectory("upload-");
            Path filePath = tempDir.resolve(fileName);
            uploadedFile.transferTo(filePath.toFile());
            logger.info("File uploaded successfully: {}", fileName);
            return filePath.toFile();
        } catch (IOException e) {
            logger.error("Failed to process uploaded file: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * @deprecated This method uses Swing GUI components not suitable for cloud environments.
     * Use processUploadedFile() instead for web-based file uploads.
     */
    @Deprecated
    public static File ReadFile(String dialogMEssage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        logger.warn("ReadFile with GUI is deprecated for cloud environments. Use web-based file upload instead.");
        throw new UnsupportedOperationException(
            "GUI-based file selection is not supported in cloud environments. Use web-based file upload instead."
        );
    }

}
