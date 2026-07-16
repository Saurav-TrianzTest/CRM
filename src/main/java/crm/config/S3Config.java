package crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration for AWS S3 client (AWS SDK for Java v2).
 *
 * The S3Client is configured from environment variables / application properties
 * following 12-factor app principles.  Credentials are resolved automatically by
 * the DefaultCredentialsProvider chain (IAM role, environment variables, or
 * ~/.aws/credentials — in that order).
 */
@Configuration
public class S3Config {

    /**
     * AWS region resolved from the environment variable AWS_REGION or the
     * application property aws.region.  Defaults to us-east-1 when absent.
     */
    @Value("${aws.region:${AWS_REGION:us-east-1}}")
    private String awsRegion;

    /**
     * Produces a singleton S3Client bean available for injection throughout
     * the application (e.g. PdfController).
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
