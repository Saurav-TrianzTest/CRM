# Cloud Readiness Fixes - GCP Migration

## Overview
This document describes the cloud readiness fixes applied to make the CRM application compatible with Google Cloud Platform (GCP) deployment.

## Changes Applied

### 1. Google Cloud Storage Integration (Blockers 1, 2, 3)

#### Files Modified:
- `pom.xml` - Added GCS dependencies
- `src/main/java/crm/utils/ReadDataUtils.java` - Replaced file system operations with GCS
- `src/main/java/crm/controller/PdfController.java` - Replaced local file writes with GCS uploads
- `src/main/java/crm/csv/CSVTest.java` - Replaced java.io.File with GCS operations

#### Changes:
- **Hard-coded file paths eliminated**: Replaced absolute file system dependencies with GCS bucket operations
- **Local file writes migrated**: PDF generation now writes to GCS instead of ephemeral local filesystem
- **java.io.File operations replaced**: CSV processing now reads from GCS using byte streams

#### Configuration Required:
Set these environment variables for GCP deployment:
```bash
GCS_BUCKET_NAME=your-crm-bucket
GCS_PROJECT_ID=your-gcp-project-id
GCS_PDF_FOLDER=pdfs/
GCS_CSV_FOLDER=csv/
```

### 2. UTC Timezone Standardization (Blockers 4, 5)

#### Files Modified:
- `src/main/java/crm/controller/DateTimeTestController.java` - Standardized on UTC timestamps
- `src/main/resources/application.properties` - Added UTC timezone configuration

#### Changes:
- **Eliminated local timezone dependencies**: All date/time operations now use UTC (ZoneOffset.UTC)
- **Consistent timestamps across regions**: Ensures time-related logic works correctly in distributed cloud environments
- **Timezone context separated**: User timezone stored separately for display purposes only

#### Configuration:
```properties
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
spring.jackson.time-zone=UTC
user.timezone=UTC  # For display purposes only
```

### 3. Configuration Management

#### Files Modified:
- `src/main/resources/application.properties` - Externalized all configuration

#### Changes:
- **Database credentials externalized**: Now use environment variables
- **GCS configuration externalized**: Bucket names and paths configurable via environment
- **12-factor app compliance**: All configuration via environment variables

#### Environment Variables:
```bash
# Database
DATABASE_URL=jdbc:mysql://your-cloud-sql-instance/crm
DATABASE_USERNAME=your-db-user
DATABASE_PASSWORD=your-db-password
DB_DDL_AUTO=validate

# Google Cloud Storage
GCS_BUCKET_NAME=your-crm-bucket
GCS_PROJECT_ID=your-gcp-project-id
GCS_PDF_FOLDER=pdfs/
GCS_CSV_FOLDER=csv/

# Timezone
USER_TIMEZONE=UTC
```

## Dependencies Added

```xml
<!-- Google Cloud Storage -->
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-storage</artifactId>
    <version>2.17.2</version>
</dependency>
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-core</artifactId>
    <version>2.9.4</version>
</dependency>
```

## GCP Deployment Prerequisites

### 1. Create GCS Bucket
```bash
gsutil mb -p your-project-id -c STANDARD -l us-central1 gs://your-crm-bucket/
```

### 2. Set up Service Account
```bash
gcloud iam service-accounts create crm-app-sa \
    --display-name="CRM Application Service Account"

gcloud projects add-iam-policy-binding your-project-id \
    --member="serviceAccount:crm-app-sa@your-project-id.iam.gserviceaccount.com" \
    --role="roles/storage.objectAdmin"
```

### 3. Configure Application Default Credentials
For local testing:
```bash
gcloud auth application-default login
```

For GCP deployment (Cloud Run, GKE, etc.):
- Service account credentials are automatically available
- No additional configuration needed

## Testing the Changes

### Local Testing with GCS
1. Set up GCP credentials:
   ```bash
   export GOOGLE_APPLICATION_CREDENTIALS=/path/to/service-account-key.json
   ```

2. Set environment variables:
   ```bash
   export GCS_BUCKET_NAME=your-test-bucket
   export GCS_PROJECT_ID=your-project-id
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Verify GCS Integration
1. Generate a PDF via `/pdf-generator` endpoint
2. Check GCS bucket for uploaded PDF:
   ```bash
   gsutil ls gs://your-crm-bucket/pdfs/
   ```

3. Test CSV reading by placing a CSV file in GCS:
   ```bash
   gsutil cp sample.csv gs://your-crm-bucket/csv/
   ```

## Cloud-Native Benefits

1. **Data Persistence**: Files stored in GCS survive container restarts and scaling events
2. **Scalability**: Multiple container instances can access the same GCS bucket
3. **Durability**: GCS provides 99.999999999% durability
4. **Regional Availability**: Data accessible across multiple regions
5. **Cost Efficiency**: Pay only for storage used, no need for persistent volumes
6. **Timezone Consistency**: UTC standardization prevents time-related bugs in distributed systems

## Migration Notes

### Breaking Changes
- `ReadDataUtils.ReadFile()` method removed (was GUI-based, not cloud-compatible)
- New methods: `readFileFromGCS()`, `downloadFileFromGCS()`, `listFilesInGCS()`
- CSV processing now requires files to be in GCS bucket

### Backward Compatibility
- For local development, set `GCS_BUCKET_NAME` to a test bucket
- Database configuration still supports localhost for local testing
- All changes maintain existing business logic

## Next Steps

1. **Set up Cloud SQL**: Migrate from local MySQL to Cloud SQL for MySQL
2. **Configure Cloud Scheduler**: For any scheduled tasks (replaces java.util.Timer)
3. **Set up Cloud Monitoring**: Configure logging and monitoring
4. **Implement Secret Manager**: For sensitive credentials
5. **Configure Cloud CDN**: For static assets if needed

## Support

For issues or questions about these cloud readiness fixes, refer to:
- Google Cloud Storage documentation: https://cloud.google.com/storage/docs
- Spring Boot on GCP: https://spring.io/guides/gs/spring-boot-for-azure/
- 12-Factor App methodology: https://12factor.net/
