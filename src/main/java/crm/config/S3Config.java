package crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration that exposes an Amazon S3 client as a managed bean.
 *
 * The AWS region is resolved from the environment variable AWS_REGION
 * (defaults to us-east-1 when not set).  Credentials are resolved
 * automatically by the AWS Default Credential Provider Chain:
 *   1. Environment variables (AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY)
 *   2. Java system properties
 *   3. AWS credentials file (~/.aws/credentials)
 *   4. IAM role attached to the EC2 / ECS / Lambda execution environment
 *
 * No credentials are hard-coded here, following the 12-factor app principle
 * of externalising configuration.
 */
@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        String awsRegion = System.getenv("AWS_REGION");
        if (awsRegion == null || awsRegion.isEmpty()) {
            awsRegion = "us-east-1";
        }
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
