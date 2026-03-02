# Cloud Deployment Guide - CRM Application

## Overview
This application has been updated to be cloud-native and ready for deployment on AWS.

## Key Changes for Cloud Readiness

### 1. Configuration Management
- **Before**: Hardcoded database credentials and URLs
- **After**: Environment variables with AWS Secrets Manager integration
- All configuration externalized via application.properties with environment variable support

### 2. File Storage
- **Before**: Local file system operations for PDF generation and CSV reading
- **After**: AWS S3 integration for cloud-native file storage
- Desktop GUI components replaced with web-based multipart file uploads

### 3. Database Connection
- **Before**: Direct JDBC connections
- **After**: HikariCP connection pooling with cloud-optimized settings

### 4. Logging
- **Before**: System.out.println and unstructured logging
- **After**: Structured JSON logging via Logback with CloudWatch integration

### 5. Framework Version
- **Before**: Spring Boot 1.5.10 (outdated)
- **After**: Spring Boot 2.7.18 (stable, production-ready)

### 6. Security
- **Before**: Actuator endpoints exposed without security
- **After**: Secured actuator endpoints with health/metrics exposure

## Environment Variables

### Required for Production:
```bash
# Database
DB_URL=jdbc:mysql://your-rds-endpoint:3306/crm?useSSL=true
DB_USERNAME=<from-secrets-manager>
DB_PASSWORD=<from-secrets-manager>

# AWS
AWS_REGION=us-east-1
S3_BUCKET_NAME=crm-app-bucket

# Application
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

# Monitoring
CLOUDWATCH_METRICS_ENABLED=true
CLOUDWATCH_NAMESPACE=crm-app
```

## AWS Services Required

### 1. Amazon RDS (MySQL)
- Create RDS MySQL instance
- Configure security groups to allow ECS access
- Store credentials in AWS Secrets Manager

### 2. Amazon S3
- Create S3 bucket for PDF storage
- Configure bucket policies for ECS task role access

### 3. Amazon ECS (Fargate)
- Container orchestration platform
- Use provided `taskdef.json` for task definition

### 4. AWS Secrets Manager
- Store database credentials securely
- Reference in ECS task definition

### 5. Amazon CloudWatch
- Logs: `/ecs/crm-app` log group
- Metrics: Custom namespace `crm-app`

## Deployment Steps

### 1. Build Docker Image
```bash
docker build -t crm-app:latest .
```

### 2. Push to Amazon ECR
```bash
# Authenticate
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Tag and push
docker tag crm-app:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
```

### 3. Create ECS Task Definition
```bash
# Update taskdef.json with your AWS account details
aws ecs register-task-definition --cli-input-json file://taskdef.json
```

### 4. Deploy to ECS
```bash
aws ecs create-service \
  --cluster crm-cluster \
  --service-name crm-app \
  --task-definition crm-app \
  --desired-count 2 \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[subnet-xxx],securityGroups=[sg-xxx],assignPublicIp=ENABLED}"
```

## CI/CD with AWS CodePipeline

Use the provided `buildspec.yml` for AWS CodeBuild integration:
1. Source: GitHub/CodeCommit
2. Build: CodeBuild (uses buildspec.yml)
3. Deploy: ECS (uses imagedefinitions.json)

## Testing Cloud Deployment

### Health Check
```bash
curl http://<load-balancer-url>/actuator/health
```

### Metrics
```bash
curl http://<load-balancer-url>/actuator/metrics
```

## Monitoring

### CloudWatch Logs
- Navigate to CloudWatch Logs
- Find log group: `/ecs/crm-app`
- View structured JSON logs

### CloudWatch Metrics
- Custom namespace: `crm-app`
- Available metrics: JVM, HTTP, database connections

## Troubleshooting

### Common Issues:

1. **Database Connection Failures**
   - Check security group rules (ECS → RDS)
   - Verify credentials in Secrets Manager
   - Check RDS endpoint configuration

2. **S3 Access Denied**
   - Verify ECS task role has S3 permissions
   - Check bucket policies

3. **Container Startup Failures**
   - Review CloudWatch Logs
   - Check environment variables in task definition

## Cost Optimization

1. Use Fargate Spot for non-production environments
2. Enable S3 lifecycle policies for old PDFs
3. Configure RDS automatic scaling
4. Use CloudWatch Logs retention policies

## Security Best Practices

1. Never commit secrets to version control
2. Use AWS Secrets Manager for all credentials
3. Enable VPC endpoints for S3 and Secrets Manager
4. Use security groups to restrict access
5. Enable AWS WAF on Application Load Balancer

## Support
For issues or questions, contact the DevOps team.
