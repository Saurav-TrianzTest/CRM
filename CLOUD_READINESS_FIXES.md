# Cloud Readiness Fixes - ModCRM3

## Overview
This document describes the cloud readiness fixes applied to the ModCRM3 application to make it compatible with AWS cloud deployment.

## Changes Applied

### 1. File System Dependencies → Amazon S3 Storage

#### ReadDataUtils.java (Blocker-1: cr-java-0061)
- **Issue**: Hard-coded file paths with JFileChooser (GUI-based file selection)
- **Fix**: Replaced with S3-based file reading using AWS SDK for Java v2
- **Impact**: Application can now read files from S3 buckets instead of local file system
- **Configuration**: Requires `AWS_S3_BUCKET_NAME` environment variable

#### PdfController.java (Blocker-2: cr-java-0062)
- **Issue**: Local file system write operations using FileOutputStream
- **Fix**: Replaced with S3 PutObject operations
- **Impact**: PDFs are now stored in S3 with durability and scalability
- **Configuration**: 
  - `AWS_S3_BUCKET_NAME` - S3 bucket for PDF storage
  - `AWS_S3_PDF_PREFIX` - Prefix/folder for PDFs (default: "pdfs/")

#### CSVTest.java (Blocker-3: cr-java-0063)
- **Issue**: java.io.File usage for CSV file operations
- **Fix**: Replaced with S3 GetObject operations
- **Impact**: CSV files are read from S3 instead of local file system
- **Configuration**:
  - `CSV_BUCKET_NAME` - S3 bucket for CSV files
  - `CSV_S3_KEY` - S3 object key for CSV file

### 2. Time/Clock Dependencies → java.time API with UTC

#### DateTimeTestController.java (Blockers 4 & 5: cr-java-0111)
- **Issue**: Usage of java.util.Date which relies on server-local timezone
- **Fix**: Migrated to java.time API (Instant, ZonedDateTime) with UTC standardization
- **Impact**: Consistent time handling across distributed cloud environments
- **Changes**:
  - Replaced `new Date()` with `Instant.now()`
  - Replaced `LocalDateTime.now()` with `LocalDateTime.now(ZoneOffset.UTC)`
  - Added `ZonedDateTime.now(ZoneOffset.UTC)` for timezone-aware operations

## New Dependencies Added

### AWS SDK for Java v2
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.26</version>
</dependency>
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>auth</artifactId>
    <version>2.20.26</version>
</dependency>
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>regions</artifactId>
    <version>2.20.26</version>
</dependency>
```

## Configuration Files

### application.properties
Updated with cloud-ready configuration using environment variables:

```properties
# Database configuration (externalized)
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/crm?useSSL=false}
spring.datasource.username=${DATABASE_USERNAME:root}
spring.datasource.password=${DATABASE_PASSWORD:password}

# AWS S3 Configuration
aws.s3.bucket.name=${AWS_S3_BUCKET_NAME:crm-pdf-bucket}
aws.s3.pdf.prefix=${AWS_S3_PDF_PREFIX:pdfs/}
aws.s3.csv.bucket=${AWS_S3_CSV_BUCKET:crm-csv-bucket}

# AWS Region
aws.region=${AWS_REGION:us-east-1}

# Timezone standardization
spring.jackson.time-zone=UTC
```

### New Configuration Class: AwsS3Config.java
- Provides S3Client bean for dependency injection
- Uses DefaultCredentialsProvider for IAM role-based authentication
- Automatically works with ECS task roles, EKS service accounts, and EC2 instance profiles

## Environment Variables Required for Cloud Deployment

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `AWS_REGION` | AWS region for S3 operations | us-east-1 | No |
| `AWS_S3_BUCKET_NAME` | S3 bucket for PDF storage | crm-pdf-bucket | Yes |
| `AWS_S3_PDF_PREFIX` | Prefix for PDF objects in S3 | pdfs/ | No |
| `AWS_S3_CSV_BUCKET` | S3 bucket for CSV files | crm-csv-bucket | Yes |
| `DATABASE_URL` | JDBC URL for database | localhost:3306/crm | Yes |
| `DATABASE_USERNAME` | Database username | root | Yes |
| `DATABASE_PASSWORD` | Database password | password | Yes |

## IAM Permissions Required

The application requires the following IAM permissions:

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
        "arn:aws:s3:::crm-pdf-bucket/*",
        "arn:aws:s3:::crm-csv-bucket/*",
        "arn:aws:s3:::crm-pdf-bucket",
        "arn:aws:s3:::crm-csv-bucket"
      ]
    }
  ]
}
```

## Cloud Deployment Readiness

### ✅ Fixed Issues
1. **File System Dependencies**: All file operations now use S3
2. **Hard-coded Paths**: Replaced with environment variables
3. **Time/Timezone Issues**: Standardized on UTC using java.time API
4. **Configuration Management**: Externalized via environment variables

### 🔧 Remaining Considerations
1. **Database**: Update DATABASE_URL to point to cloud-managed database (RDS)
2. **S3 Buckets**: Create required S3 buckets before deployment
3. **IAM Roles**: Attach appropriate IAM role to ECS task or EKS pod
4. **Secrets Management**: Consider using AWS Secrets Manager for DATABASE_PASSWORD

## Testing in Cloud Environment

1. **Create S3 Buckets**:
   ```bash
   aws s3 mb s3://crm-pdf-bucket
   aws s3 mb s3://crm-csv-bucket
   ```

2. **Set Environment Variables** in your container/pod definition

3. **Verify IAM Role** has required S3 permissions

4. **Deploy and Test**:
   - PDF generation should store files in S3
   - CSV processing should read from S3
   - Time operations should use UTC consistently

## Migration Notes

- **Backward Compatibility**: Local file operations are completely replaced. If you need to support both local and S3, implement a storage abstraction layer.
- **Data Migration**: Existing local files need to be uploaded to S3 before deployment.
- **Testing**: Update integration tests to use S3 mock services (e.g., LocalStack).

## Support

For issues or questions about these cloud readiness fixes, refer to:
- AWS SDK for Java v2 Documentation: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/
- Spring Boot on AWS: https://spring.io/guides/gs/spring-boot-aws/
