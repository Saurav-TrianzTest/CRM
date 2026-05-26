package crm.utils;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-ready utility for reading data files.
 * Uses AWS Systems Manager Parameter Store for externalized file path configuration.
 * Falls back to classpath resources for cloud-native file access.
 */
public class ReadDataUtils {

    private static final String DEFAULT_PARAMETER_NAME = "/crm/file/base-path";
    
    /**
     * Reads a file from a cloud-compatible location.
     * First attempts to retrieve the file path from AWS Systems Manager Parameter Store.
     * If not available, falls back to classpath resource loading.
     * 
     * @param fileName the name of the file to read
     * @return File object or null if not found
     */
    public static File ReadFile(String fileName) {
        try {
            // Attempt to get file path from AWS Systems Manager Parameter Store
            String basePath = getFilePathFromParameterStore(DEFAULT_PARAMETER_NAME);
            if (basePath != null && !basePath.isEmpty()) {
                File file = new File(basePath, fileName);
                if (file.exists()) {
                    return file;
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to retrieve file path from Parameter Store: " + e.getMessage());
        }
        
        // Fallback to classpath resource
        try {
            Resource resource = new ClassPathResource(fileName);
            if (resource.exists()) {
                return resource.getFile();
            }
        } catch (IOException e) {
            System.err.println("Unable to load file from classpath: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Reads a file as an InputStream from classpath (cloud-compatible).
     * 
     * @param fileName the name of the file to read
     * @return InputStream or null if not found
     */
    public static InputStream ReadFileAsStream(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            if (resource.exists()) {
                return resource.getInputStream();
            }
        } catch (IOException e) {
            System.err.println("Unable to load file from classpath: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves file path configuration from AWS Systems Manager Parameter Store.
     * 
     * @param parameterName the parameter name in Parameter Store
     * @return the parameter value (file path) or null if not found
     */
    private static String getFilePathFromParameterStore(String parameterName) {
        try {
            AWSSimpleSystemsManagement ssmClient = AWSSimpleSystemsManagementClientBuilder.defaultClient();
            GetParameterRequest request = new GetParameterRequest()
                    .withName(parameterName)
                    .withWithDecryption(true);
            GetParameterResult result = ssmClient.getParameter(request);
            return result.getParameter().getValue();
        } catch (Exception e) {
            System.err.println("Error retrieving parameter from SSM: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Legacy method maintained for backward compatibility.
     * Note: This method uses Swing components which are not suitable for cloud/server environments.
     * Consider using ReadFile(String fileName) instead for cloud deployments.
     * 
     * @deprecated Use ReadFile(String fileName) or ReadFileAsStream(String fileName) instead
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, javax.swing.JFrame parent, String fileExtensionDescription,
                                String... fileExtension) {
        System.err.println("WARNING: This method uses Swing components and is not suitable for cloud environments.");
        System.err.println("Please use ReadFile(String fileName) or ReadFileAsStream(String fileName) instead.");
        return null;
    }

}
