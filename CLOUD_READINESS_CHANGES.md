# Cloud Readiness Transformation Report

## Overview
This document describes the cloud readiness fixes applied to the CRM application to make it compatible with AWS cloud deployment.

## Issues Fixed

### 1. Hard-coded File Paths (cr-java-0061)
**Location:** `crm/utils/ReadDataUtils.java` (Line 12)
**Issue:** Application used absolute file paths with JFileChooser, creating dependencies on local file system.
**Fix Applied:**
- Replaced JFileChooser-based file selection with Amazon S3 object storage
- Implemented `readFileFromS3()` method using AWS SDK for Java v2
- File operations now use S3 keys instead of local file paths
- Configuration via environment variables: `S3_BUCKET_NAME`, `AWS_REGION`

### 2. Local File System Write Operations (cr-java-0062)
**Location:** `crm/controller/PdfController.java` (Line 35)
**Issue:** Application wrote PDF files directly to local file system using FileOutputStream.
**Fix Applied:**
- Replaced FileOutputStream with ByteArrayOutputStream for in-memory PDF generation
- Implemented S3 upload using AWS SDK v2 S3Client
- PDFs now stored in S3 bucket under `pdfs/` prefix
- Ensures data durability and availability in containerized environments

### 3. Java.io.File Usage for Data Storage (cr-java-0063)
**Location:** `crm/csv/CSVTest.java` (Line 21)
**Issue:** Application used java.io.File API for persistent data storage.
**Fix Applied:**
- Replaced File-based operations with S3 InputStream
- CSV files now read from S3 using S3Client
- Removed dependency on local file system
- Configuration via environment variable: `CSV_S3_KEY`

### 4. Clock/Time Dependencies (cr-java-0111)
**Location:** `crm/controller/DateTimeTestController.java` (Lines 19-20)
**Issue:** Application used java.util.Date which relies on server-local timezone.
**Fix Applied:**
- Replaced java.util.Date with java.time.Instant for UTC timestamps
- Added ZonedDateTime with UTC (ZoneOffset.UTC) for timezone-aware operations
- All LocalDateTime and LocalDate operations now use UTC
- Standardized on UTC across all time-sensitive operations
- Added Hibernate timezone configuration: `spring.jpa.properties.hibernate.jdbc.time_zone=UTC`

## New Components Added

### 1. S3Config.java
- Spring configuration class for AWS S3Client bean
- Configures S3Client with region from environment variable
- Enables dependency injection of S3Client throughout the application

### 2. S3Service.java
- Centralized service for S3 operations
- Provides `uploadFile()` and `downloadFile()` methods
- Handles error logging and exception management
- Configurable bucket name via environment variable

## Configuration Changes

### application.properties
- Externalized database configuration using environment variables:
  - `DATABASE_URL`
  - `DATABASE_USERNAME`
  - `DATABASE_PASSWORD`
  - `DB_DDL_AUTO`
- Added UTC timezone configuration for Hibernate
- Documented required AWS environment variables

### pom.xml
- Added AWS SDK for Java v2 dependencies:
  - `software.amazon.awssdk:s3:2.17.100`
  - `software.amazon.awssdk:bom:2.17.100`

## Environment Variables Required

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `S3_BUCKET_NAME` | S3 bucket for file storage | `crm-data-bucket` |
| `AWS_REGION` | AWS region for S3 services | `us-east-1` |
| `DATABASE_URL` | JDBC connection URL | `jdbc:mysql://localhost:3306/crm?useSSL=false` |
| `DATABASE_USERNAME` | Database username | `root` |
| `DATABASE_PASSWORD` | Database password | `password` |
| `DB_DDL_AUTO` | Hibernate DDL mode | `update` |
| `CSV_S3_KEY` | S3 key for CSV test file | `data/sample.csv` |

## AWS IAM Permissions Required

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
        "arn:aws:s3:::crm-data-bucket/*",
        "arn:aws:s3:::crm-data-bucket"
      ]
    }
  ]
}
```

## Deployment Considerations

1. **S3 Bucket Setup**: Create S3 bucket(s) before deployment
2. **IAM Roles**: Assign appropriate IAM role to ECS task or EC2 instance
3. **Environment Variables**: Configure all required environment variables
4. **Database**: Use RDS or managed database service with proper security groups
5. **Timezone**: Application now standardized on UTC for all operations

## Testing Recommendations

1. Test S3 file upload/download operations
2. Verify PDF generation and S3 storage
3. Test CSV reading from S3
4. Validate UTC timestamp handling across different regions
5. Test database connectivity with environment variable configuration

## Benefits Achieved

- ✅ Eliminated local file system dependencies
- ✅ Achieved data durability with S3 storage
- ✅ Enabled horizontal scaling without state issues
- ✅ Standardized on UTC to prevent timezone issues
- ✅ Externalized configuration for cloud deployment
- ✅ Followed 12-factor app principles
- ✅ Made application fully cloud-native and container-ready
