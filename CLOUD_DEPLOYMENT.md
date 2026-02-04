# Cloud Deployment Guide

## Overview
This application has been updated for cloud-native deployment on AWS. All cloud readiness issues have been addressed.

## Changes Made

### 1. Configuration Management
- Externalized all configuration to environment variables
- Replaced hardcoded database credentials with `${DB_USERNAME}` and `${DB_PASSWORD}`
- Replaced hardcoded database URL with `${DB_URL}`
- Added HikariCP connection pooling for cloud scalability
- Enabled SSL/TLS for database connections
- Secured management endpoints

### 2. File System Dependencies
- Removed local file system writes in PDF generation
- Changed PDF generation to use ByteArrayOutputStream (in-memory)
- Removed Swing/AWT GUI components (ReadDataUtils.java)
- Prepared for S3 integration for file storage

### 3. Security
- Migrated from deprecated `WebSecurityConfigurerAdapter` to `SecurityFilterChain`
- Added proper authentication for management endpoints
- Enabled SSL for database connections
- Removed test credentials from source control comments

### 4. Dependencies
- Upgraded Spring Boot from 1.5.10 to 2.7.18
- Upgraded Java from 8 to 11
- Updated all vulnerable dependencies:
  - iTextPDF: 5.5.13 → 5.5.13.3
  - Apache POI: 3.15 → 5.2.5
  - PDFBox: 2.0.8 → 2.0.30
  - BouncyCastle: 1.59 → 1.70
  - NekoHTML: 1.9.21 → 1.9.22
- Added AWS SDK for S3 integration
- Added structured logging with Logstash encoder

### 5. Logging
- Added logback-spring.xml with JSON format for CloudWatch
- Configured structured logging with correlation IDs
- Added profile-specific logging (local vs cloud)

## Environment Variables Required

### Database Configuration
```bash
DB_URL=jdbc:mysql://<rds-endpoint>:3306/crm?useSSL=true&requireSSL=true
DB_USERNAME=<db-username>
DB_PASSWORD=<db-password>
```

### Optional Configuration
```bash
DB_DDL_AUTO=validate                    # Use 'validate' in production
DB_POOL_SIZE=20                         # Connection pool size
MANAGEMENT_SECURITY_ENABLED=true        # Secure actuator endpoints
S3_BUCKET_NAME=crm-pdfs                 # S3 bucket for PDF storage
AWS_REGION=us-east-1                    # AWS region
SPRING_PROFILES_ACTIVE=cloud            # Activate cloud profile
```

## AWS Deployment Steps

### 1. Build Docker Image
```bash
docker build -t crm-app:latest .
```

### 2. Tag and Push to ECR
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com
docker tag crm-app:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
```

### 3. Deploy to ECS/EKS

#### ECS Task Definition Example
```json
{
  "family": "crm-app",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "1024",
  "memory": "2048",
  "containerDefinitions": [{
    "name": "crm-app",
    "image": "<account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest",
    "portMappings": [{"containerPort": 8080, "protocol": "tcp"}],
    "environment": [
      {"name": "SPRING_PROFILES_ACTIVE", "value": "cloud"}
    ],
    "secrets": [
      {"name": "DB_URL", "valueFrom": "arn:aws:secretsmanager:..."},
      {"name": "DB_USERNAME", "valueFrom": "arn:aws:secretsmanager:..."},
      {"name": "DB_PASSWORD", "valueFrom": "arn:aws:secretsmanager:..."}
    ],
    "logConfiguration": {
      "logDriver": "awslogs",
      "options": {
        "awslogs-group": "/ecs/crm-app",
        "awslogs-region": "us-east-1",
        "awslogs-stream-prefix": "ecs"
      }
    },
    "healthCheck": {
      "command": ["CMD-SHELL", "wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1"],
      "interval": 30,
      "timeout": 5,
      "retries": 3,
      "startPeriod": 60
    }
  }]
}
```

### 4. Set Up RDS MySQL Database
```bash
# Create RDS MySQL instance with Multi-AZ
aws rds create-db-instance \
  --db-instance-identifier crm-db \
  --db-instance-class db.t3.medium \
  --engine mysql \
  --engine-version 8.0 \
  --master-username admin \
  --master-user-password <secure-password> \
  --allocated-storage 20 \
  --multi-az \
  --vpc-security-group-ids sg-xxxxx
```

### 5. Create S3 Bucket for PDFs
```bash
aws s3 mb s3://crm-pdfs
```

### 6. Configure Secrets Manager
```bash
aws secretsmanager create-secret \
  --name crm-db-credentials \
  --secret-string '{"username":"admin","password":"<secure-password>","host":"<rds-endpoint>","dbname":"crm"}'
```

## Monitoring

### CloudWatch Logs
- All logs are automatically sent to CloudWatch Logs
- JSON format enables advanced filtering and queries

### Health Checks
- Liveness: `/actuator/health`
- Readiness: `/actuator/health`
- Metrics: `/actuator/metrics`

### Alarms
Configure CloudWatch alarms for:
- CPU utilization > 80%
- Memory utilization > 85%
- Response time > 1000ms
- Error rate > 5%

## Known Limitations

1. **PDF Storage**: PDF generation currently uses in-memory storage. For production:
   - Implement S3 integration in `PdfController.java`
   - Add S3 client configuration
   - Store PDFs in S3 with pre-signed URLs

2. **Session Management**: Application uses HTTP sessions. For multi-instance deployments:
   - Consider using Spring Session with Redis
   - Or implement JWT-based stateless authentication

3. **CSV Import**: Commented out due to file system dependencies. For production:
   - Implement REST API endpoint for CSV uploads
   - Use S3 for temporary storage
   - Process files asynchronously using SQS

## Security Considerations

1. **Database Credentials**: Always use AWS Secrets Manager or Parameter Store
2. **SSL/TLS**: Ensure RDS SSL certificates are properly configured
3. **Network Security**: Use VPC security groups to restrict access
4. **IAM Roles**: Use IAM roles for service-to-service authentication
5. **Secrets**: Never commit credentials to source control

## Testing

### Local Testing with Docker
```bash
docker run -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/crm?useSSL=false" \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=password \
  -e SPRING_PROFILES_ACTIVE=cloud \
  crm-app:latest
```

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

## Support

For issues or questions regarding cloud deployment, refer to:
- Spring Boot Cloud documentation
- AWS ECS/EKS documentation
- AWS RDS MySQL documentation
