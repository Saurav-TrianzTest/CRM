package crm.utils;

import java.io.File;

/**
 * @deprecated This class uses GUI components (JFileChooser) that are incompatible with containerized environments.
 * Use file upload endpoints with Spring's MultipartFile instead.
 * For batch processing, read files from mounted volumes or cloud storage.
 */
@Deprecated
public class ReadDataUtils {

    /**
     * @deprecated This method uses JFileChooser which requires a GUI and will throw HeadlessException in containers.
     * Replace with REST endpoints that accept file uploads via HTTP multipart requests.
     */
    @Deprecated
    public static File ReadFile(String dialogMEssage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException(
            "JFileChooser is not supported in containerized environments. " +
            "Use file upload endpoints with Spring MultipartFile or read from mounted volumes."
        );
    }

}
