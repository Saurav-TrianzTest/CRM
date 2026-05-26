package crm.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Cloud configuration for AWS services and cloud-native patterns.
 * 
 * This configuration class provides:
 * 1. AWS SDK clients configured with proper credentials and region
 * 2. Clock bean for consistent time handling across the application
 * 3. Cloud-ready service configurations
 */
@Configuration
public class CloudConfig {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    /**
     * AWS Systems Manager client for Parameter Store access.
     * Uses DefaultAWSCredentialsProviderChain which checks:
     * 1. Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
     * 2. System properties
     * 3. AWS credentials file
     * 4. EC2 instance profile credentials (recommended for cloud deployments)
     * 
     * @return configured AWS SSM client
     */
    @Bean
    public AWSSimpleSystemsManagement awsSimpleSystemsManagement() {
        return AWSSimpleSystemsManagementClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.fromName(awsRegion))
                .build();
    }

    /**
     * System clock bean using UTC timezone.
     * This ensures consistent time handling across all application components
     * and makes the application testable by allowing clock injection.
     * 
     * @return Clock instance configured for UTC
     */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}
