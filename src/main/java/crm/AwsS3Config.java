package crm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration for AWS S3 client (AWS SDK for Java v2).
 *
 * The S3Client is configured using the default AWS credential provider chain,
 * which resolves credentials from (in order):
 *   1. Environment variables: AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY
 *   2. Java system properties
 *   3. AWS credentials file (~/.aws/credentials)
 *   4. IAM role attached to the EC2 instance / ECS task / Lambda function
 *
 * The AWS region is read from the {@code AWS_REGION} environment variable
 * (or the {@code aws.region} application property), defaulting to us-east-1.
 */
@Configuration
public class AwsS3Config {

    @Value("${aws.region:${AWS_REGION:us-east-1}}")
    private String awsRegion;

    /**
     * Creates and exposes a singleton {@link S3Client} bean that can be
     * injected into any Spring-managed component (e.g. {@code PdfController},
     * {@code CSVTest}).
     *
     * @return configured AWS SDK v2 S3Client
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
