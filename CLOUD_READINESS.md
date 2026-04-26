# ModCRM3 - Cloud-Ready Application

## Cloud Readiness Transformations

This application has been transformed to be fully cloud-ready and compatible with AWS cloud environments. All cloud compatibility blockers have been resolved.

### Resolved Cloud Readiness Issues

#### 1. Hard-coded File Paths (cr-java-0061) ✅
**Original Issue:** Application used absolute file paths with JFileChooser for file selection, creating dependencies on local file system.

**Resolution:** 
- Migrated `ReadDataUtils.java` to use Amazon S3 SDK v2
- Replaced `java.io.File` and `JFileChooser` with S3Client
- Files are now read from S3 buckets using object keys
- Bucket names configured via environment variables

**Files Modified:**
- `src/main/java/crm/utils/ReadDataUtils.java`

#### 2. Local File System Write Operations (cr-java-0062) ✅
**Original Issue:** Application wrote PDF files directly to local file system using `FileOutputStream`, causing data loss in ephemeral container environments.

**Resolution:**
- Migrated `PdfController.java` to generate PDFs in memory and upload to S3
- Replaced `FileOutputStream` with `ByteArrayOutputStream` and S3 `PutObject` operations
- PDF metadata stored in database with S3 object key reference
- S3 bucket and prefix configured via environment variables

**Files Modified:**
- `src/main/java/crm/controller/PdfController.java`
- `src/main/java/crm/entity/Pdf.java` (added `s3Key` field)

#### 3. Java.io.File Usage for Data Storage (cr-java-0063) ✅
**Original Issue:** Application used `java.io.File` API for CSV file operations, assuming local file system persistence.

**Resolution:**
- Migrated `CSVTest.java` to read CSV files from S3
- Replaced `File` and `FileReader` with S3 `GetObject` operations
- CSV bucket and object keys configured via environment variables or command-line arguments
- Uses IAM roles for authentication in cloud environments

**Files Modified:**
- `src/main/java/crm/csv/CSVTest.java`

#### 4. Clock/Time Dependencies (cr-java-0111) ✅
**Original Issue:** Application used `java.util.Date` which relies on server-local timezone, causing inconsistencies in distributed cloud environments.

**Resolution:**
- Migrated `DateTimeTestController.java` from `java.util.Date` to `java.time` API
- All timestamps now use UTC (`ZoneOffset.UTC`) for consistency
- Replaced `Date` with `Instant`, `LocalDateTime`, and `ZonedDateTime`
- Standardized timezone handling across all cloud regions

**Files Modified:**
- `src/main/java/crm/controller/DateTimeTestController.java`

### New Cloud-Native Components

#### AWS S3 Configuration
**File:** `src/main/java/crm/config/AwsS3Config.java`

Provides Spring-managed S3Client bean with:
- DefaultCredentialsProvider for IAM role support
- Environment-based region configuration
- Automatic credential resolution (IAM roles, environment variables, credentials file)

### Environment Variables

The following environment variables should be configured in your cloud deployment:

#### Required AWS Configuration
```bash
# AWS Region
AWS_REGION=us-east-1

# S3 Bucket Configuration
AWS_S3_BUCKET_NAME=your-crm-bucket
AWS_S3_PDF_PREFIX=pdfs/
AWS_S3_CSV_BUCKET=your-csv-bucket

# Database Configuration (externalized)
DATABASE_URL=jdbc:mysql://your-rds-endpoint:3306/crm?useSSL=true
DATABASE_USERNAME=your-db-user
DATABASE_PASSWORD=your-db-password
```

#### AWS Credentials (for local development only)
In cloud environments (ECS, EKS, Lambda), use IAM roles instead:
```bash
# Only for local development
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
```

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
        "arn:aws:s3:::your-crm-bucket/*",
        "arn:aws:s3:::your-crm-bucket",
        "arn:aws:s3:::your-csv-bucket/*",
        "arn:aws:s3:::your-csv-bucket"
      ]
    }
  ]
}
```

### Maven Dependencies Added

```xml
<!-- AWS SDK for Java v2 - S3 Client -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.17.100</version>
</dependency>
```

### Cloud Deployment Readiness

✅ **File System Independence:** All file operations use S3 object storage  
✅ **Stateless Design:** No local state dependencies  
✅ **Externalized Configuration:** All configuration via environment variables  
✅ **Timezone Consistency:** UTC standardization for distributed environments  
✅ **IAM Role Support:** Uses DefaultCredentialsProvider for cloud authentication  
✅ **12-Factor App Compliance:** Configuration, backing services, stateless processes  

### Testing in Cloud Environment

1. **Create S3 Buckets:**
   ```bash
   aws s3 mb s3://your-crm-bucket
   aws s3 mb s3://your-csv-bucket
   ```

2. **Set Environment Variables:**
   Configure the environment variables listed above in your container orchestration platform (ECS, EKS, etc.)

3. **Attach IAM Role:**
   Ensure your ECS task role or EKS service account has the required S3 permissions

4. **Deploy Application:**
   The application will automatically use IAM roles for S3 access in cloud environments

### Migration Notes

- **PDF Storage:** All generated PDFs are now stored in S3. The `Pdf` entity includes an `s3Key` field to reference the S3 object location.
- **CSV Processing:** CSV files must be uploaded to S3 before processing. The application reads from S3 instead of local file system.
- **Time Handling:** All date/time operations use UTC. Ensure client applications handle timezone conversion if needed.

### Backward Compatibility

This version is NOT backward compatible with local file system operations. All file operations now require S3 connectivity. For local development:

1. Use LocalStack or MinIO to simulate S3 locally
2. Or configure AWS credentials for development S3 buckets

### Support

For issues related to cloud deployment, check:
- AWS CloudWatch Logs for application logs
- S3 bucket permissions and IAM role configuration
- Environment variable configuration in your deployment platform
