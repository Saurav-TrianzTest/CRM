# Cloud Readiness Fixes - CRM Application

## Overview
This document describes the cloud-native transformations applied to make the CRM application fully compatible with AWS cloud deployment.

## Fixed Cloud Readiness Issues

### 1. Hard-coded File Paths (cr-java-0061)
**File:** `src/main/java/crm/utils/ReadDataUtils.java`
**Issue:** Application used absolute file paths with JFileChooser for local file system access
**Fix Applied:**
- Replaced local file system operations with Amazon S3 SDK for Java v2
- Implemented `readFileFromS3()` methods to retrieve files from S3 buckets
- Added support for configurable bucket names via environment variables
- Removed dependency on javax.swing (GUI components not suitable for cloud)

### 2. Local File System Write Operations (cr-java-0062)
**File:** `src/main/java/crm/controller/PdfController.java`
**Issue:** PDF files were written directly to local file system using FileOutputStream
**Fix Applied:**
- Replaced `FileOutputStream` with in-memory `ByteArrayOutputStream`
- Implemented `generateAndUploadPdfToS3()` to upload PDFs directly to S3
- Added S3Client dependency injection
- Configured S3 bucket name and prefix via environment variables
- Updated Pdf entity to store S3 object key reference
- Added proper error handling for S3 operations

### 3. Java.io.File Usage for Data Storage (cr-java-0063)
**File:** `src/main/java/crm/csv/CSVTest.java`
**Issue:** Used java.io.File with local file system for CSV processing
**Fix Applied:**
- Replaced File-based operations with S3 GetObject API
- Implemented `readCsvFromS3()` method to stream CSV data from S3
- Added environment variable configuration (AWS_S3_BUCKET_NAME, AWS_S3_CSV_KEY)
- Used S3 ResponseInputStream for efficient streaming
- Maintained existing CSV parsing logic with OpenCSV

### 4 & 5. Clock/Time Dependencies (cr-java-0111)
**File:** `src/main/java/crm/controller/DateTimeTestController.java`
**Issue:** Used java.util.Date which relies on server-local timezone
**Fix Applied:**
- Removed java.util.Date usage (lines 19-20)
- Migrated to java.time API (Instant, ZonedDateTime, LocalDateTime)
- Standardized all timestamps to UTC using ZoneOffset.UTC
- Added ZonedDateTime with explicit UTC timezone
- Ensures consistent time handling across distributed cloud environments

## New Configuration Files

### AWS S3 Configuration Class
**File:** `src/main/java/crm/config/AwsS3Config.java`
- Provides S3Client bean for dependency injection
- Uses DefaultCredentialsProvider for secure credential management
- Supports multiple credential sources (environment variables, IAM roles, credentials file)
- Configurable AWS region via environment variable

## Updated Configuration

### application.properties
Added cloud-native configuration:
```properties
# Database configuration with environment variables
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/crm?useSSL=false}
spring.datasource.username=${DATABASE_USERNAME:root}
spring.datasource.password=${DATABASE_PASSWORD:password}

# AWS S3 Configuration
aws.s3.bucket.name=${AWS_S3_BUCKET_NAME:crm-pdf-storage}
aws.s3.pdf.prefix=${AWS_S3_PDF_PREFIX:pdfs/}
aws.s3.csv.prefix=${AWS_S3_CSV_PREFIX:csv/}
aws.region=${AWS_REGION:us-east-1}

# UTC timezone standardization
spring.jackson.time-zone=UTC
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

### pom.xml
Added AWS SDK for Java v2 dependencies:
- `software.amazon.awssdk:s3` (version 2.20.26)
- `software.amazon.awssdk:core` (version 2.20.26)
- `software.amazon.awssdk:auth` (version 2.20.26)
- `software.amazon.awssdk:regions` (version 2.20.26)

## Environment Variables Required for AWS Deployment

### Required for S3 Operations:
- `AWS_S3_BUCKET_NAME` - S3 bucket name for file storage
- `AWS_REGION` - AWS region (default: us-east-1)

### Required for Database:
- `DATABASE_URL` - JDBC connection URL
- `DATABASE_USERNAME` - Database username
- `DATABASE_PASSWORD` - Database password

### AWS Credentials (one of the following):
- `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` (for development)
- IAM Role attached to EC2/ECS/EKS (recommended for production)

### Optional Configuration:
- `AWS_S3_PDF_PREFIX` - S3 prefix for PDF files (default: pdfs/)
- `AWS_S3_CSV_PREFIX` - S3 prefix for CSV files (default: csv/)
- `AWS_S3_CSV_KEY` - Specific S3 key for CSV test operations

## Cloud Deployment Readiness

### 12-Factor App Compliance:
✅ **Configuration** - All configuration externalized via environment variables
✅ **Backing Services** - Database and S3 treated as attached resources
✅ **Stateless Processes** - No local file system dependencies
✅ **Port Binding** - Spring Boot embedded server (configurable via SERVER_PORT)
✅ **Disposability** - Fast startup, graceful shutdown
✅ **Dev/Prod Parity** - Same S3 storage pattern across environments

### AWS Service Integration:
- **Amazon S3** - Durable, scalable object storage for files
- **Amazon RDS** - Managed database service (MySQL compatible)
- **IAM Roles** - Secure credential management without hardcoded keys
- **CloudWatch** - Compatible logging format (structured JSON recommended)

### Container Readiness:
- No host-specific file paths
- No GUI dependencies (removed javax.swing)
- Environment-based configuration
- Stateless design suitable for horizontal scaling

## Testing Recommendations

### Local Testing with LocalStack:
```bash
# Start LocalStack for S3 emulation
docker run -d -p 4566:4566 localstack/localstack

# Configure environment
export AWS_S3_BUCKET_NAME=test-bucket
export AWS_REGION=us-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_ENDPOINT_URL=http://localhost:4566
```

### AWS Deployment Testing:
1. Create S3 bucket: `aws s3 mb s3://crm-pdf-storage`
2. Configure IAM role with S3 permissions
3. Set environment variables in ECS/EKS task definition
4. Deploy and verify S3 operations in CloudWatch logs

## Migration Notes

### Breaking Changes:
- `ReadDataUtils.ReadFile()` method signature changed - now requires S3Client and bucket name
- `CSVTest.main()` now requires AWS_S3_BUCKET_NAME and AWS_S3_CSV_KEY environment variables
- PDF files are no longer stored locally - retrieve from S3 using stored s3Key

### Data Migration:
If migrating existing local files to S3:
```bash
# Upload existing PDFs to S3
aws s3 sync ./local-pdf-directory/ s3://crm-pdf-storage/pdfs/

# Upload CSV files
aws s3 cp data.csv s3://crm-pdf-storage/csv/data.csv
```

## Security Considerations

### IAM Policy for Application:
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
        "arn:aws:s3:::crm-pdf-storage",
        "arn:aws:s3:::crm-pdf-storage/*"
      ]
    }
  ]
}
```

### Best Practices:
- Use IAM roles instead of access keys in production
- Enable S3 bucket encryption at rest
- Enable S3 bucket versioning for data protection
- Use VPC endpoints for S3 access from private subnets
- Implement S3 lifecycle policies for cost optimization

## Performance Optimization

### S3 Best Practices:
- Use multipart upload for files >100MB
- Implement retry logic with exponential backoff
- Consider S3 Transfer Acceleration for global deployments
- Use CloudFront CDN for frequently accessed files

### Monitoring:
- Monitor S3 request metrics in CloudWatch
- Set up alarms for S3 error rates
- Track application logs for S3 operation failures
- Monitor database connection pool metrics

## Support and Troubleshooting

### Common Issues:

**Issue:** S3 access denied errors
**Solution:** Verify IAM role permissions and bucket policies

**Issue:** Timezone inconsistencies
**Solution:** All timestamps now use UTC - verify client-side timezone handling

**Issue:** Missing environment variables
**Solution:** Check application.properties for required variables and defaults

**Issue:** S3 connection timeout
**Solution:** Verify VPC security groups and S3 endpoint configuration
