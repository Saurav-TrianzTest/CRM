package crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 configuration for cloud-native storage.
 * Provides an S3Client bean configured from environment variables / application properties.
 * Replaces local file system dependencies with Amazon S3 for durable, scalable storage.
 */
@Configuration
public class AwsS3Config {

    @Value("${aws.region:#{environment.AWS_REGION ?: 'us-east-1'}}")
    private String awsRegion;

    /**
     * Creates and configures an AWS S3Client bean.
     * Credentials are resolved automatically via the AWS Default Credential Provider Chain
     * (environment variables, IAM roles, instance profiles, etc.).
     *
     * @return configured S3Client instance
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }

}
