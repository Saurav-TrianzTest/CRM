package crm.utils;

import java.io.File;

/**
 * DEPRECATED: This class contains GUI dependencies (JFileChooser) that are incompatible with containerized environments.
 * Use web-based file upload via MultipartFile in Spring controllers instead.
 * This class is kept for backward compatibility but should not be used in production.
 */
@Deprecated
public class ReadDataUtils {

    public static File ReadFile(String dialogMEssage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException(
            "GUI-based file chooser is not supported in containerized environments. " +
            "Use web-based file upload via MultipartFile instead."
        );
    }

}
