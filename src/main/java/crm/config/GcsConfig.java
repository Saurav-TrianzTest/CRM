package crm.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Google Cloud Storage configuration for cloud-native file operations.
 * Provides centralized GCS client configuration.
 */
@Configuration
public class GcsConfig {

    @Value("${gcs.project.id:}")
    private String projectId;

    @Value("${gcs.bucket.name:crm-data-bucket}")
    private String bucketName;

    /**
     * Creates a Storage client bean for Google Cloud Storage operations.
     * Uses Application Default Credentials (ADC) for authentication.
     * 
     * @return Storage client instance
     */
    @Bean
    public Storage storage() {
        StorageOptions.Builder builder = StorageOptions.newBuilder();
        
        // Set project ID if provided
        if (projectId != null && !projectId.isEmpty()) {
            builder.setProjectId(projectId);
        }
        
        return builder.build().getService();
    }

    /**
     * Provides the configured GCS bucket name.
     * 
     * @return Bucket name from configuration
     */
    @Bean
    public String gcsBucketName() {
        return bucketName;
    }
}
