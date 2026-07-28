# Cloud Readiness Fixes - CRM Application

## Overview
This document describes the cloud-native transformations applied to make the CRM application fully compatible with AWS cloud deployment.

## Changes Summary

### 1. File System Dependencies → Amazon S3 Storage

#### Fixed Files:
- `crm/utils/ReadDataUtils.java` (Blocker #1 - cr-java-0061)
- `crm/controller/PdfController.java` (Blocker #2 - cr-java-0062)
- `crm/csv/CSVTest.java` (Blocker #3 - cr-java-0063)

#### Changes:
- **Replaced**: Hard-coded file paths and local file system operations
- **With**: Amazon S3 object storage using AWS SDK for Java v2
- **Benefits**:
  - Durable, scalable storage
  - No dependency on ephemeral container file systems
  - Data persists across container restarts and scaling events
  - Multi-region availability

#### Implementation Details:
- `ReadDataUtils.java`: Now reads files from S3 buckets instead of local file system
- `PdfController.java`: Generates PDFs in memory and uploads directly to S3
- `CSVTest.java`: Reads CSV files from S3 streams instead of local File objects
- Added `AwsS3Config.java`: Spring configuration for S3Client bean with DefaultCredentialsProvider

### 2. Time/Clock Dependencies → java.time API with UTC

#### Fixed Files:
- `crm/controller/DateTimeTestController.java` (Blockers #4 & #5 - cr-java-0111)

#### Changes:
- **Replaced**: `java.util.Date` (lines 19-20)
- **With**: `java.time.Instant`, `ZonedDateTime`, and UTC-based timestamps
- **Benefits**:
  - Consistent timezone handling across distributed cloud services
  - No dependency on server-local timezone settings
  - Thread-safe, immutable date/time objects
  - Better API for cloud-native applications

#### Implementation Details:
- All timestamps now use UTC (`ZoneOffset.UTC`)
- Replaced `new Date()` with `ZonedDateTime.now(ZoneOffset.UTC)`
- Added `Instant` for precise timestamp handling
- Configured Jackson to use UTC timezone in `application.properties`

### 3. Configuration Management

#### Updated Files:
- `pom.xml`: Added AWS SDK v2 dependencies
- `application.properties`: Externalized configuration with environment variables
- `crm/entity/Pdf.java`: Added `s3Key` field to store S3 object references

#### Changes:
- **Database Configuration**: Now uses environment variables
  - `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
- **AWS Configuration**: Added S3 bucket and region configuration
  - `AWS_REGION`, `AWS_S3_BUCKET_NAME`, `AWS_S3_PDF_PREFIX`, `AWS_S3_CSV_PREFIX`
- **Timezone**: Standardized to UTC for all JSON serialization

## AWS SDK Dependencies Added

```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.26</version>
</dependency>
```

## Environment Variables Required

### Database (Existing - now externalized):
- `DATABASE_URL`: JDBC connection string (default: `jdbc:mysql://localhost:3306/crm?useSSL=false`)
- `DATABASE_USERNAME`: Database username (default: `root`)
- `DATABASE_PASSWORD`: Database password (default: `password`)

### AWS S3 (New):
- `AWS_REGION`: AWS region for S3 (default: `us-east-1`)
- `AWS_S3_BUCKET_NAME`: S3 bucket name for storage (default: `crm-storage-bucket`)
- `AWS_S3_PDF_PREFIX`: S3 prefix for PDF files (default: `pdfs/`)
- `AWS_S3_CSV_PREFIX`: S3 prefix for CSV files (default: `csv/`)

### AWS Credentials (Handled by DefaultCredentialsProvider):
The application uses AWS SDK's DefaultCredentialsProvider which automatically detects credentials from:
1. Environment variables: `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`
2. System properties
3. AWS credentials file (`~/.aws/credentials`)
4. IAM instance profile (EC2)
5. ECS container credentials
6. EKS pod credentials (IRSA)

## Deployment Considerations

### AWS IAM Permissions Required
The application requires the following S3 permissions:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject",
        "s3:ListBucket"
      ],
      "Resource": [
        "arn:aws:s3:::your-bucket-name/*",
        "arn:aws:s3:::your-bucket-name"
      ]
    }
  ]
}
```

### Recommended AWS Services
- **Compute**: AWS ECS (Fargate), EKS, or EC2
- **Storage**: Amazon S3 for file storage
- **Database**: Amazon RDS for MySQL
- **Secrets**: AWS Secrets Manager or Parameter Store for credentials

### 12-Factor App Compliance
✅ **Config**: All configuration externalized via environment variables
✅ **Backing Services**: Database and S3 treated as attached resources
✅ **Stateless Processes**: No local file system dependencies
✅ **Logs**: Structured logging to stdout (cloud-native)
✅ **Disposability**: Fast startup, graceful shutdown

## Testing Locally

### Prerequisites:
1. AWS credentials configured (via AWS CLI or environment variables)
2. S3 bucket created
3. MySQL database running

### Run with environment variables:
```bash
export AWS_REGION=us-east-1
export AWS_S3_BUCKET_NAME=my-crm-bucket
export DATABASE_URL=jdbc:mysql://localhost:3306/crm?useSSL=false
export DATABASE_USERNAME=root
export DATABASE_PASSWORD=password

mvn spring-boot:run
```

## Migration Notes

### Data Migration:
If you have existing PDFs or CSV files in local storage:
1. Upload existing files to S3 bucket
2. Update database records with S3 keys
3. Remove local file references

### Backward Compatibility:
The changes are **not backward compatible** with local file system storage. All file operations now require S3 connectivity.

## Verification Checklist

- [x] Hard-coded file paths removed
- [x] Local file write operations replaced with S3
- [x] java.io.File usage migrated to S3
- [x] java.util.Date replaced with java.time API
- [x] UTC timezone standardization implemented
- [x] Configuration externalized to environment variables
- [x] AWS SDK v2 dependencies added
- [x] S3Client configuration created
- [x] All business logic preserved
- [x] Cloud-native patterns implemented

## Support

For issues or questions regarding these cloud-native transformations, refer to:
- AWS SDK for Java v2 Documentation: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/
- Spring Boot on AWS: https://spring.io/guides/gs/spring-boot-aws/
- 12-Factor App Methodology: https://12factor.net/
