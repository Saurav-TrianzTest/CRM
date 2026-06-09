package crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration for AWS S3 client (AWS SDK for Java v2).
 *
 * The S3Client is configured to resolve credentials and region automatically
 * from the standard AWS credential provider chain:
 *   1. Environment variables  (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_SESSION_TOKEN)
 *   2. Java system properties
 *   3. AWS credentials file (~/.aws/credentials)
 *   4. IAM role attached to the EC2 / ECS / Lambda execution environment
 *
 * The target AWS region is resolved from the AWS_REGION environment variable.
 * If not set, the SDK falls back to the default region provider chain.
 */
@Configuration
public class S3ClientConfig {

    @Bean
    public S3Client s3Client() {
        String regionEnv = System.getenv("AWS_REGION");
        if (regionEnv != null && !regionEnv.isEmpty()) {
            return S3Client.builder()
                    .region(Region.of(regionEnv))
                    .build();
        }
        // Fall back to the SDK's default region provider chain
        return S3Client.create();
    }
}
