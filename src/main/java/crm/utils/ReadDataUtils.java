package crm.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * @deprecated This class uses GUI components (JFileChooser) which are incompatible with containerized headless environments.
 * Do not use this class in production. Use REST API file upload endpoints instead.
 * For file imports, implement HTTP multipart form upload or accept file paths via configuration.
 */
@Deprecated
public class ReadDataUtils {

    /**
     * @deprecated GUI-based file selection cannot work in containerized server environments.
     * Use REST API endpoints with file upload capability instead.
     */
    @Deprecated
    public static File ReadFile(String dialogMEssage, JFrame parent, String fileExtensionDescription,
                                String... fileExtension) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileExtensionDescription, fileExtension);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            return chooser.getSelectedFile();
        }
        return null;
    }

}
