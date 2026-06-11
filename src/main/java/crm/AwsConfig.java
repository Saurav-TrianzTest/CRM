package crm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration for AWS SDK v2 beans.
 *
 * The S3Client is configured using the default credential provider chain
 * (environment variables, instance profile, ECS task role, etc.) and the
 * AWS region is read from the AWS_REGION environment variable, falling back
 * to us-east-1 if not set.  This ensures the application is fully portable
 * across AWS environments without hard-coded credentials or regions.
 */
@Configuration
public class AwsConfig {

    @Bean
    public S3Client s3Client() {
        String regionName = System.getenv("AWS_REGION");
        Region region = (regionName != null && !regionName.isEmpty())
                ? Region.of(regionName)
                : Region.US_EAST_1;

        return S3Client.builder()
                .region(region)
                .build();
    }
}
