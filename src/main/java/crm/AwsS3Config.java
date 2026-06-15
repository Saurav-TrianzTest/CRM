package crm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration for AWS S3 client (AWS SDK for Java v2).
 * The S3Client is configured using the default credential provider chain,
 * which supports environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY),
 * IAM instance profiles, ECS task roles, and other cloud-native credential sources.
 */
@Configuration
public class AwsS3Config {

    @Value("${aws.region:${AWS_REGION:us-east-1}}")
    private String awsRegion;

    /**
     * Creates a singleton S3Client bean using the default credential provider chain.
     * Credentials are resolved automatically from:
     *   1. Environment variables: AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY
     *   2. AWS shared credentials file (~/.aws/credentials)
     *   3. EC2/ECS instance profile / task role (recommended for cloud deployments)
     *
     * @return configured S3Client
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }

}
