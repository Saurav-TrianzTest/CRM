# AWS Cloud Deployment Guide

## Overview
This application has been migrated to be cloud-ready and compatible with AWS deployment. All cloud compatibility issues have been resolved.

## Cloud-Ready Features

### 1. Configuration Management
- All hardcoded credentials replaced with environment variables
- Database connection uses AWS RDS endpoints
- HikariCP connection pooling configured for cloud scalability
- Supports AWS Secrets Manager for secure credential management

### 2. File Storage
- PDF generation now uses AWS S3 for storage instead of local file system
- CSV file reading supports both classpath resources and S3
- No local file system dependencies

### 3. Security
- Management endpoints secured by default
- Plaintext passwords no longer exposed in PDF exports
- IAM role-based authentication for AWS services

### 4. Monitoring & Logging
- Structured logging with SLF4J
- Health check endpoints available for load balancers
- Metrics endpoints for CloudWatch integration

## Prerequisites

1. AWS Account with appropriate permissions
2. RDS MySQL database instance
3. S3 buckets for PDF and CSV storage
4. IAM roles with appropriate policies
5. Java 11 runtime

## AWS Resources Required

### RDS Database
```bash
# Create RDS MySQL instance
aws rds create-db-instance \
  --db-instance-identifier crm-db \
  --db-instance-class db.t3.small \
  --engine mysql \
  --master-username admin \
  --master-user-password <secure-password> \
  --allocated-storage 20 \
  --vpc-security-group-ids sg-xxxxx
```

### S3 Buckets
```bash
# Create S3 buckets
aws s3 mb s3://crm-pdf-storage-bucket
aws s3 mb s3://crm-csv-data-bucket

# Enable versioning
aws s3api put-bucket-versioning \
  --bucket crm-pdf-storage-bucket \
  --versioning-configuration Status=Enabled
```

### IAM Policy
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject",
        "s3:DeleteObject",
        "s3:ListBucket"
      ],
      "Resource": [
        "arn:aws:s3:::crm-pdf-storage-bucket/*",
        "arn:aws:s3:::crm-csv-data-bucket/*"
      ]
    },
    {
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue"
      ],
      "Resource": "arn:aws:secretsmanager:us-east-1:*:secret:crm/*"
    }
  ]
}
```

## Deployment Options

### Option 1: AWS Elastic Beanstalk

1. Package the application:
```bash
mvn clean package
```

2. Create Elastic Beanstalk application:
```bash
eb init -p "Corretto 11" crm-app --region us-east-1
eb create crm-prod
```

3. Configure environment variables:
```bash
eb setenv \
  DB_URL=jdbc:mysql://your-rds-endpoint:3306/crm \
  DB_USERNAME=admin \
  DB_PASSWORD=<from-secrets-manager> \
  AWS_REGION=us-east-1 \
  PDF_STORAGE_BUCKET=crm-pdf-storage-bucket \
  PDF_STORAGE_ENABLED=true
```

4. Deploy:
```bash
eb deploy
```

### Option 2: AWS ECS with Fargate

1. Create Dockerfile:
```dockerfile
FROM amazoncorretto:11
COPY target/crm-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

2. Build and push Docker image:
```bash
docker build -t crm-app .
aws ecr create-repository --repository-name crm-app
docker tag crm-app:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
```

3. Create ECS task definition with environment variables

4. Create ECS service with load balancer

### Option 3: AWS Lambda (Requires additional configuration)

Use AWS Serverless Application Model (SAM) or Spring Cloud Function adapter.

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| DB_URL | RDS MySQL endpoint | jdbc:mysql://host:3306/crm |
| DB_USERNAME | Database username | admin |
| DB_PASSWORD | Database password | Use Secrets Manager |
| AWS_REGION | AWS region | us-east-1 |
| PDF_STORAGE_BUCKET | S3 bucket for PDFs | crm-pdf-storage-bucket |
| CSV_S3_BUCKET | S3 bucket for CSV files | crm-csv-data-bucket |
| MANAGEMENT_SECURITY_ENABLED | Enable security | true |

## Health Checks

The application exposes health check endpoints:
- `/appinfo/health` - Health status
- `/appinfo/info` - Application info
- `/appinfo/metrics` - Application metrics

Configure your load balancer to use `/appinfo/health` for health checks.

## Monitoring

### CloudWatch Integration
- Application logs are written to stdout/stderr
- Configure CloudWatch Logs agent to capture container logs
- Set up CloudWatch alarms for error rates and latency

### Metrics
- Use Spring Boot Actuator metrics endpoint
- Export metrics to CloudWatch using Micrometer

## Security Best Practices

1. Use AWS Secrets Manager for database credentials
2. Use IAM roles instead of access keys
3. Enable encryption at rest for RDS and S3
4. Use VPC security groups to restrict database access
5. Enable SSL/TLS for database connections
6. Regularly update dependencies for security patches

## Troubleshooting

### Database Connection Issues
- Verify security group allows inbound traffic from ECS/EB
- Check RDS endpoint is correct
- Verify IAM role has rds-db:connect permission

### S3 Access Issues
- Verify IAM role has s3:GetObject and s3:PutObject permissions
- Check bucket names are correct
- Verify AWS_REGION matches bucket region

### Application Not Starting
- Check CloudWatch logs for error messages
- Verify all required environment variables are set
- Check Java version is 11 or higher

## Cost Optimization

1. Use RDS reserved instances for production
2. Configure S3 lifecycle policies to archive old PDFs
3. Use Application Load Balancer with target tracking
4. Enable auto-scaling based on CPU/memory metrics
5. Use Spot instances for non-production environments

## Rollback Plan

1. Keep previous application version in S3
2. Use Elastic Beanstalk version management for quick rollback
3. Maintain database backups with point-in-time recovery
4. Test rollback procedure in staging environment

## Support

For issues or questions, refer to AWS documentation:
- Elastic Beanstalk: https://docs.aws.amazon.com/elasticbeanstalk/
- ECS: https://docs.aws.amazon.com/ecs/
- RDS: https://docs.aws.amazon.com/rds/
