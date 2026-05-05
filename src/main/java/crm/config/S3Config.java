package crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 Configuration for cloud-native file storage.
 * Replaces local file system dependencies with Amazon S3.
 */
@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        String awsRegion = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
        
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
