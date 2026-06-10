package crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 configuration for cloud-native object storage.
 * Provides a Spring-managed S3Client bean that uses the AWS default credential chain:
 * environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY),
 * IAM instance roles, or ECS task roles in cloud environments.
 */
@Configuration
public class S3Config {

    @Value("${aws.region:${AWS_REGION:us-east-1}}")
    private String awsRegion;

    /**
     * Creates an S3Client bean using the AWS SDK default credential provider chain.
     * In cloud environments (EC2, ECS, Lambda), credentials are automatically
     * resolved from the attached IAM role — no hard-coded credentials needed.
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
