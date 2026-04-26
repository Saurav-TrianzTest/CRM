package crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 Configuration for cloud-native file storage.
 * Provides S3Client bean configured with default AWS credentials provider chain.
 * 
 * The DefaultCredentialsProvider automatically handles credentials from:
 * - Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
 * - System properties
 * - AWS credentials file (~/.aws/credentials)
 * - IAM role for EC2/ECS/EKS (recommended for production)
 */
@Configuration
public class AwsS3Config {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    /**
     * Creates and configures S3Client bean for dependency injection.
     * Uses default credentials provider chain for secure credential management.
     * 
     * @return Configured S3Client instance
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
