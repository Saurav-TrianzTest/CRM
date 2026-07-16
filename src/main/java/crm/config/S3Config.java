package crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration that exposes an AWS SDK v2 {@link S3Client} bean.
 *
 * Credentials are resolved automatically via the AWS Default Credential Provider
 * Chain (IAM role → environment variables → ~/.aws/credentials), so no secrets
 * are hard-coded in source code or configuration files.
 *
 * The AWS region is read from the {@code aws.region} application property which
 * should be supplied via an environment variable (AWS_REGION) or an external
 * configuration source, following 12-factor app principles.
 */
@Configuration
public class S3Config {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
