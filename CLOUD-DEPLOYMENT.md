# Cloud Deployment Guide

This application has been updated to be cloud-ready and compatible with AWS cloud environments.

## Cloud Readiness Fixes Applied

### 1. Configuration Management
- Replaced hardcoded database credentials with environment variables
- Replaced hardcoded localhost URLs with configurable environment variables
- Added HikariCP connection pooling for cloud database connections
- Created cloud-specific configuration profile

### 2. File System Dependencies
- Replaced local file system writes with configurable storage paths
- Updated PDF generation to use ByteArrayOutputStream and configurable storage
- Replaced Java Swing file chooser with cloud-compatible classpath/storage reading
- Updated CSV reading to support classpath resources and environment-based storage

### 3. Logging and Monitoring
- Replaced System.out.println with structured SLF4J logging
- Added JSON logging support for cloud monitoring (Logstash)
- Configured cloud-native logging patterns
- Added correlation IDs support for distributed tracing

### 4. Framework Updates
- Updated Spring Boot from 1.5.10 to 2.7.18 (latest LTS version)
- Updated Java version from 8 to 11 for better cloud performance
- Updated Thymeleaf security extras to Spring Security 5
- Added AWS SDK support for S3 storage (optional)

### 5. Cloud Architecture
- Added Dockerfile for containerized deployment
- Created health check endpoints for liveness/readiness probes
- Configured graceful shutdown for zero-downtime deployments
- Added AWS ECS deployment configuration

## Environment Variables

Set these environment variables for cloud deployment:

### Database Configuration
- `DB_URL`: Database connection URL (e.g., jdbc:mysql://rds-endpoint:3306/crm)
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `DB_DDL_AUTO`: Hibernate DDL mode (validate/update/none)
- `DB_POOL_SIZE`: Maximum connection pool size (default: 20)

### Server Configuration
- `PORT`: Server port (default: 8080)
- `SPRING_PROFILES_ACTIVE`: Active Spring profile (use "cloud" for cloud deployment)

### Storage Configuration
- `PDF_STORAGE_PATH`: Path for PDF storage (default: /tmp/pdfs)
- `FILE_STORAGE_PATH`: Path for file storage (default: /tmp/files)
- `CSV_FILE_NAME`: CSV file name to process (default: data.csv)

### Logging Configuration
- `LOG_LEVEL`: Root logging level (default: INFO)
- `APP_LOG_LEVEL`: Application logging level (default: DEBUG)

## AWS Deployment Steps

### 1. Build Docker Image
```bash
docker build -t crm-app:latest .
```

### 2. Push to Amazon ECR
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com
docker tag crm-app:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest
```

### 3. Deploy to ECS
```bash
# Update the aws-deployment.yaml with your ECR registry
# Deploy using AWS ECS CLI or CloudFormation
```

### 4. Configure RDS Database
- Create an RDS MySQL instance
- Set DB_URL, DB_USERNAME, and DB_PASSWORD environment variables
- Ensure security groups allow ECS tasks to connect to RDS

### 5. Configure EFS for File Storage (Optional)
- Create an EFS file system
- Mount it to /app/storage in ECS tasks
- Update PDF_STORAGE_PATH and FILE_STORAGE_PATH accordingly

## Health Checks

The application provides the following health check endpoints:

- `/actuator/health/liveness` - Liveness probe
- `/actuator/health/readiness` - Readiness probe
- `/actuator/health` - Overall health status
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application information

## Cloud-Native Features

1. **12-Factor App Compliance**: Configuration via environment variables
2. **Stateless Design**: No local state storage, cloud-compatible
3. **Connection Pooling**: HikariCP for efficient database connections
4. **Graceful Shutdown**: Proper cleanup on container termination
5. **Health Probes**: Kubernetes/ECS-compatible health checks
6. **Structured Logging**: JSON logs for cloud monitoring systems
7. **Resource Limits**: Configurable memory and CPU limits
8. **Auto-scaling Ready**: Stateless design supports horizontal scaling

## Testing Cloud Deployment

Run locally with cloud profile:
```bash
export SPRING_PROFILES_ACTIVE=cloud
export DB_URL=jdbc:mysql://localhost:3306/crm
export DB_USERNAME=root
export DB_PASSWORD=password
mvn spring-boot:run
```

Test with Docker:
```bash
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/crm \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=password \
  -e SPRING_PROFILES_ACTIVE=cloud \
  crm-app:latest
```

## Monitoring and Observability

The application exports metrics in Prometheus format at `/actuator/prometheus` for integration with:
- Amazon CloudWatch
- Prometheus + Grafana
- Datadog
- New Relic

Structured JSON logs can be ingested by:
- AWS CloudWatch Logs
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Splunk
- Datadog Logs

## Security Considerations

1. Never commit credentials to version control
2. Use AWS Secrets Manager or Parameter Store for sensitive data
3. Enable SSL/TLS for database connections in production
4. Use IAM roles for AWS service access
5. Run containers as non-root user (configured in Dockerfile)
6. Keep dependencies updated for security patches

## Troubleshooting

### Application won't start
- Check database connectivity: `telnet <rds-endpoint> 3306`
- Verify environment variables are set correctly
- Check CloudWatch logs for startup errors

### File operations failing
- Verify storage paths are writable
- Check EFS mount if using EFS
- Ensure container has proper permissions

### Database connection issues
- Verify security group allows inbound traffic from ECS
- Check RDS endpoint and credentials
- Review connection pool settings

## Next Steps

1. Set up CI/CD pipeline (AWS CodePipeline, GitHub Actions)
2. Configure auto-scaling policies
3. Set up CloudWatch alarms for monitoring
4. Implement distributed tracing (AWS X-Ray)
5. Configure backup and disaster recovery
