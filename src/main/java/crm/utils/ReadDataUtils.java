package crm.utils;

import java.io.File;

public class ReadDataUtils {

    /**
     * @deprecated This method uses GUI components (JFileChooser) which are not compatible with containerized environments.
     * For production use, implement file path specification via configuration or environment variables.
     * This method is retained only for backward compatibility in development/testing scenarios.
     */
    @Deprecated
    public static File ReadFile(String dialogMEssage, java.awt.Component parent, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException(
            "GUI file selection is not supported in containerized environments. " +
            "Please use programmatic file path specification via configuration or environment variables."
        );
    }

}
