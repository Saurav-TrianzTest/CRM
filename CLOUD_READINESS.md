# Cloud Readiness Transformation Report

## Overview
This document describes the cloud readiness transformations applied to the CRM application to make it compatible with AWS cloud environments.

## Transformations Applied

### 1. Hard-coded File Paths (cr-java-0061)
**File:** `src/main/java/crm/utils/ReadDataUtils.java`

**Issue:** Application contained absolute file paths that reference specific locations on the host file system.

**Remediation Applied:**
- Replaced hard-coded paths with AWS Systems Manager Parameter Store integration
- Added fallback to classpath resources for cloud-native file access
- Implemented `ReadFile(String fileName)` method that retrieves file paths from Parameter Store
- Added `ReadFileAsStream(String fileName)` for streaming file access from classpath
- Deprecated legacy Swing-based file chooser method (not suitable for server environments)

**Configuration Required:**
- Set AWS Systems Manager Parameter: `/crm/file/base-path` with the base directory path
- Alternatively, place files in classpath resources for bundled access

---

### 2. Local File System Write Operations (cr-java-0062)
**File:** `src/main/java/crm/controller/PdfController.java`

**Issue:** Application performed direct write operations to local file system locations, causing data loss in ephemeral container environments.

**Remediation Applied:**
- Configured PDF storage to use Amazon EFS (Elastic File System) mounted volume
- Added `pdf.storage.path` configuration property (default: `/mnt/efs/pdfs`)
- Implemented directory creation with proper error handling
- Added alternative in-memory PDF generation method for stateless operations
- Enhanced logging for troubleshooting file operations

**Configuration Required:**
- Mount Amazon EFS volume to container at `/mnt/efs` (or configure custom path)
- Set environment variable: `PDF_STORAGE_PATH=/mnt/efs/pdfs`
- Ensure EFS volume has proper read/write permissions

**AWS Setup:**
1. Create Amazon EFS file system in your VPC
2. Create mount targets in each availability zone
3. Configure security groups to allow NFS traffic (port 2049)
4. Mount EFS to EC2 instances or ECS tasks using mount helper or task definition

---

### 3. Java.io.File Usage for Data Storage (cr-java-0063)
**File:** `src/main/java/crm/csv/CSVTest.java`

**Issue:** Application used Java File API for persistent data storage instead of cloud-native storage services.

**Remediation Applied:**
- Implemented dual-mode CSV reading: Amazon EFS and classpath resources
- Added `readCsvFromEFS(String fileName)` method for EFS-based file access
- Added `readCsvFromClasspath(String resourcePath)` for bundled resources
- Configured CSV storage path via environment variable (default: `/mnt/efs/csv`)
- Enhanced main method to support command-line arguments for source selection

**Configuration Required:**
- Mount Amazon EFS volume for CSV storage
- Set environment variable: `CSV_STORAGE_PATH=/mnt/efs/csv`
- For static reference data, place CSV files in `src/main/resources`

**Usage Examples:**
```bash
# Read from EFS
java crm.csv.CSVTest efs sample.csv

# Read from classpath
java crm.csv.CSVTest classpath data/sample.csv
```

---

### 4. Clock/Time Dependencies (cr-java-0111)
**Files:** 
- `src/main/java/crm/controller/DateTimeTestController.java` (lines 19-20)

**Issue:** Application relied on server-local timezone settings and used legacy `java.util.Date` API.

**Remediation Applied:**
- Migrated from `java.util.Date` to `java.time` API (Instant, ZonedDateTime, Clock)
- Standardized all time operations on UTC timezone
- Implemented Clock abstraction for testability and consistency
- Added explicit timezone handling with `ZonedDateTime` when timezone context is required
- Configured Jackson to serialize dates in ISO 8601 format with UTC timezone
- Added helper methods for timestamp parsing and timezone conversion

**Best Practices Implemented:**
- Store all timestamps in UTC (using `Instant`)
- Use `ZonedDateTime` only when timezone context is required
- Convert to local timezone only at presentation layer
- Use ISO 8601 format for all timestamp serialization

**Configuration Added:**
```properties
spring.jackson.time-zone=UTC
spring.jackson.serialization.write-dates-as-timestamps=false
```

---

## Configuration Summary

### Environment Variables
The following environment variables should be configured for cloud deployment:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DB_URL` | Database JDBC URL | `jdbc:mysql://localhost:3306/crm?useSSL=false` | Yes |
| `DB_USERNAME` | Database username | `root` | Yes |
| `DB_PASSWORD` | Database password | `password` | Yes |
| `DB_DDL_AUTO` | Hibernate DDL mode | `create-drop` | No |
| `PDF_STORAGE_PATH` | EFS mount path for PDFs | `/mnt/efs/pdfs` | Yes |
| `CSV_STORAGE_PATH` | EFS mount path for CSVs | `/mnt/efs/csv` | Yes |
| `AWS_REGION` | AWS region | `us-east-1` | No |

### AWS Systems Manager Parameters
Configure the following parameters in AWS Systems Manager Parameter Store:

| Parameter Name | Description | Type |
|----------------|-------------|------|
| `/crm/file/base-path` | Base directory for file operations | String |

### AWS Services Required

1. **Amazon EFS (Elastic File System)**
   - Purpose: Persistent, shared file storage for PDFs and CSVs
   - Configuration: Create EFS file system and mount to containers
   - Security: Configure security groups for NFS access (port 2049)

2. **AWS Systems Manager Parameter Store**
   - Purpose: Externalized configuration management
   - Configuration: Store sensitive configuration and file paths
   - Access: Ensure IAM role has `ssm:GetParameter` permission

3. **IAM Roles and Permissions**
   - EC2/ECS task role should have:
     - `ssm:GetParameter` for Parameter Store access
     - `elasticfilesystem:ClientMount` for EFS access
     - `elasticfilesystem:ClientWrite` for EFS write operations

### Maven Dependencies Added

```xml
<!-- AWS SDK for Systems Manager Parameter Store -->
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-ssm</artifactId>
    <version>1.11.1000</version>
</dependency>
```

---

## Deployment Checklist

### Pre-Deployment
- [ ] Create Amazon EFS file system in target VPC
- [ ] Configure EFS mount targets in all availability zones
- [ ] Set up security groups for NFS access
- [ ] Create IAM role with required permissions
- [ ] Configure AWS Systems Manager parameters
- [ ] Set environment variables in deployment configuration

### Container/EC2 Configuration
- [ ] Mount EFS volume to `/mnt/efs` (or custom path)
- [ ] Verify EFS mount is accessible and writable
- [ ] Configure AWS credentials (use IAM role for EC2/ECS)
- [ ] Set AWS region in environment or allow auto-detection

### Application Configuration
- [ ] Update database connection string for cloud database (RDS)
- [ ] Configure database credentials via environment variables
- [ ] Verify PDF storage path is accessible
- [ ] Verify CSV storage path is accessible
- [ ] Test Parameter Store connectivity

### Post-Deployment Verification
- [ ] Verify application can read from Parameter Store
- [ ] Test PDF generation and storage to EFS
- [ ] Test CSV reading from EFS and classpath
- [ ] Verify timestamps are in UTC
- [ ] Check application logs for any file system errors

---

## Testing in Cloud Environment

### Local Testing with AWS LocalStack
For local development and testing, you can use LocalStack to simulate AWS services:

```bash
# Start LocalStack with SSM and EFS
docker run -d -p 4566:4566 -p 4571:4571 localstack/localstack

# Configure AWS CLI to use LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1

# Create test parameter
aws --endpoint-url=http://localhost:4566 ssm put-parameter \
    --name /crm/file/base-path \
    --value /tmp/crm-files \
    --type String
```

### Integration Testing
1. Deploy to AWS test environment
2. Verify EFS mount is accessible
3. Test file upload and download operations
4. Verify data persistence across container restarts
5. Test multi-instance concurrent access to EFS

---

## Troubleshooting

### Issue: Cannot connect to Parameter Store
**Solution:** 
- Verify IAM role has `ssm:GetParameter` permission
- Check AWS region configuration
- Verify network connectivity to AWS services

### Issue: Cannot write to EFS
**Solution:**
- Verify EFS is mounted correctly
- Check file system permissions
- Verify security group allows NFS traffic (port 2049)
- Check EFS mount target is in the same VPC/subnet

### Issue: Timezone inconsistencies
**Solution:**
- Verify `spring.jackson.time-zone=UTC` is configured
- Check all date/time operations use `Clock` bean
- Ensure database stores timestamps in UTC

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     AWS Cloud Environment                    │
│                                                               │
│  ┌──────────────┐         ┌──────────────┐                  │
│  │   ECS/EC2    │         │   ECS/EC2    │                  │
│  │  Container   │         │  Container   │                  │
│  │              │         │              │                  │
│  │  CRM App     │         │  CRM App     │                  │
│  │  /mnt/efs ───┼─────────┼──────────────┼──┐               │
│  └──────────────┘         └──────────────┘  │               │
│         │                        │           │               │
│         │                        │           ▼               │
│         │                        │    ┌─────────────┐        │
│         │                        │    │  Amazon EFS │        │
│         │                        │    │  (Shared    │        │
│         │                        │    │   Storage)  │        │
│         │                        │    └─────────────┘        │
│         │                        │                           │
│         ▼                        ▼                           │
│  ┌──────────────────────────────────┐                       │
│  │   AWS Systems Manager            │                       │
│  │   Parameter Store                │                       │
│  │   - /crm/file/base-path          │                       │
│  └──────────────────────────────────┘                       │
│                                                               │
│         │                        │                           │
│         ▼                        ▼                           │
│  ┌──────────────────────────────────┐                       │
│  │   Amazon RDS (MySQL)             │                       │
│  │   - Managed Database             │                       │
│  └──────────────────────────────────┘                       │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## Additional Recommendations

### 1. Database Connection Pooling
Consider adding HikariCP configuration for production:
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

### 2. Secrets Management
For production, use AWS Secrets Manager instead of environment variables:
```java
// Example: Retrieve database password from Secrets Manager
AWSSecretsManager client = AWSSecretsManagerClientBuilder.defaultClient();
GetSecretValueRequest request = new GetSecretValueRequest()
    .withSecretId("crm/db/password");
GetSecretValueResult result = client.getSecretValue(request);
String password = result.getSecretString();
```

### 3. Monitoring and Logging
- Enable CloudWatch Logs for application logs
- Configure CloudWatch metrics for EFS performance
- Set up alarms for disk space and IOPS

### 4. Backup and Disaster Recovery
- Enable EFS automatic backups
- Configure RDS automated backups
- Test restore procedures regularly

---

## Compliance and Security

### Data at Rest
- EFS supports encryption at rest using AWS KMS
- RDS supports encryption at rest
- Enable encryption for production deployments

### Data in Transit
- EFS supports encryption in transit (TLS)
- Use SSL/TLS for database connections
- Configure security groups to restrict access

### Access Control
- Use IAM roles instead of access keys
- Follow principle of least privilege
- Regularly audit IAM permissions

---

## Support and Maintenance

For questions or issues related to cloud readiness transformations:
1. Review this documentation
2. Check application logs for error messages
3. Verify AWS service configurations
4. Consult AWS documentation for service-specific issues

---

**Document Version:** 1.0  
**Last Updated:** 2025-01-24  
**Transformation ID:** cloudreadiness-fix-2025-01-24
