package crm.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AWS Configuration for cloud-ready deployment.
 * Provides centralized AWS service client beans with proper credential management.
 */
@Configuration
public class AwsConfig {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    /**
     * Amazon S3 client bean for durable object storage.
     * Uses DefaultAWSCredentialsProviderChain which supports:
     * - Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
     * - System properties
     * - IAM instance profile credentials (recommended for EC2/ECS)
     * - Container credentials (for ECS tasks)
     */
    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(awsRegion))
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }

    /**
     * AWS Systems Manager client bean for Parameter Store access.
     * Used for externalized configuration management.
     */
    @Bean
    public AWSSimpleSystemsManagement awsSimpleSystemsManagement() {
        return AWSSimpleSystemsManagementClientBuilder.standard()
                .withRegion(Regions.fromName(awsRegion))
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }
}
