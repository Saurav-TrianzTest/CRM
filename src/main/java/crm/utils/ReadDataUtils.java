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
 * Uses AWS Systems Manager Parameter Store for externalized configuration
 * and classpath resources for cloud-compatible file access.
 */
public class ReadDataUtils {

    private static final String DEFAULT_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
    
    /**
     * Retrieves file path from AWS Systems Manager Parameter Store.
     * 
     * @param parameterName The parameter name in SSM Parameter Store
     * @return The file path value from Parameter Store
     */
    public static String getFilePathFromParameterStore(String parameterName) {
        AWSSimpleSystemsManagement ssmClient = AWSSimpleSystemsManagementClientBuilder
                .standard()
                .withRegion(DEFAULT_REGION)
                .build();
        
        GetParameterRequest request = new GetParameterRequest()
                .withName(parameterName)
                .withWithDecryption(true);
        
        GetParameterResult result = ssmClient.getParameter(request);
        return result.getParameter().getValue();
    }
    
    /**
     * Reads a file from classpath resources (cloud-compatible).
     * 
     * @param resourcePath The path to the resource in classpath
     * @return InputStream for the resource
     * @throws IOException if resource cannot be found
     */
    public static InputStream readFileFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        return resource.getInputStream();
    }
    
    /**
     * Gets a File reference from classpath (for backward compatibility).
     * Note: This should be used carefully in cloud environments.
     * 
     * @param resourcePath The path to the resource in classpath
     * @return File object
     * @throws IOException if resource cannot be found
     */
    public static File getFileFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        return resource.getFile();
    }
    
    /**
     * Legacy method - deprecated in favor of cloud-native approaches.
     * This method relied on JFileChooser which is not suitable for cloud/headless environments.
     * 
     * @deprecated Use getFilePathFromParameterStore() or readFileFromClasspath() instead
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException(
            "JFileChooser is not supported in cloud/headless environments. " +
            "Use getFilePathFromParameterStore() or readFileFromClasspath() instead."
        );
    }

}
