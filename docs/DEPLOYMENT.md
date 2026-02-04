# Spring Boot CRM Application - AWS ECS Fargate Deployment Guide

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Local Development Setup](#local-development-setup)
4. [Building and Pushing Docker Images](#building-and-pushing-docker-images)
5. [AWS ECS Fargate Prerequisites](#aws-ecs-fargate-prerequisites)
6. [ECS Task Definition Explained](#ecs-task-definition-explained)
7. [ECS Service Configuration](#ecs-service-configuration)
8. [AWS ECS Fargate Deployment](#aws-ecs-fargate-deployment)
9. [Configuration Management](#configuration-management)
10. [Monitoring and Logging](#monitoring-and-logging)
11. [Troubleshooting](#troubleshooting)
12. [Scaling and Management](#scaling-and-management)
13. [Security Best Practices](#security-best-practices)
14. [Technology-Specific Notes](#technology-specific-notes)

---

## Overview

This guide covers the deployment of a **Spring Boot 1.5.10 CRM application** built with Java 8 and Maven to **AWS ECS Fargate**. The application is containerized using Docker and deployed as a serverless container service.

**Application Details:**
- **Framework:** Spring Boot 1.5.10
- **Java Version:** 8
- **Build Tool:** Maven
- **Package Type:** Executable JAR
- **Application Port:** 8080
- **Health Endpoint:** `/appinfo/health`
- **Management Context:** `/appinfo`

---

## Prerequisites

### Required Software

1. **Docker Desktop**
   - Version: 20.10 or later
   - Installation: [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/)

2. **AWS CLI**
   - Version: 2.x or later
   - Installation: [https://aws.amazon.com/cli/](https://aws.amazon.com/cli/)
   - Configure credentials: `aws configure`

3. **jq** (for Linux/macOS deployment scripts)
   - Installation: `sudo apt-get install jq` (Ubuntu) or `brew install jq` (macOS)

4. **Git** (for version control)
   - Version: 2.x or later

### AWS Account Requirements

1. **AWS Account** with appropriate permissions
2. **IAM User** with the following policies:
   - `AmazonECS_FullAccess`
   - `AmazonEC2ContainerRegistryFullAccess`
   - `IAMFullAccess` (for role creation)
   - `AmazonVPCFullAccess`
   - `ElasticLoadBalancingFullAccess`

3. **AWS Resources:**
   - VPC with at least 2 subnets in different availability zones
   - Security groups configured for ECS tasks
   - RDS MySQL instance (or external MySQL database)

---

## Local Development Setup

### 1. Clone the Repository

```bash
cd /modernize-data/studio-data/TNT1001/APP1236/transformed-code/401/studio-workspace/JavaUpgrade
```

### 2. Build with Docker Compose

```bash
docker-compose up --build
```

This will:
- Build the Docker image using the multi-stage Dockerfile
- Start the application container
- Expose port 8080 for local access

### 3. Access the Application

- **Application URL:** http://localhost:8080
- **Health Check:** http://localhost:8080/appinfo/health
- **Actuator Info:** http://localhost:8080/appinfo/info

### 4. Stop the Application

```bash
docker-compose down
```

---

## Building and Pushing Docker Images

### Using Linux/macOS Script

```bash
cd scripts
chmod +x build-push.sh
./build-push.sh
```

### Using Windows Script

```cmd
cd scripts
build-push.bat
```

### Script Workflow

1. **Prompts for image tag** (default: `latest`)
2. **Registry selection:**
   - Option 1: AWS ECR (Elastic Container Registry)
   - Option 2: Docker Hub
3. **Registry authentication:**
   - ECR: Uses AWS CLI to authenticate
   - Docker Hub: Prompts for username and password
4. **Auto-creates ECR repository** if it doesn't exist
5. **Builds Docker image** with sanitized tags
6. **Pushes image** to selected registry

### Example Output

```
========================================
Docker Build and Push Script
========================================

Enter image tag (default: latest): v1.0.0
Using sanitized tag: v1-0-0

Select container registry:
1. AWS ECR (Elastic Container Registry)
2. Docker Hub
Enter choice (1 or 2): 1

=== AWS ECR Configuration ===
Enter AWS Region (e.g., us-east-1): us-east-1
Enter AWS Account ID: 123456789012
Enter ECR Repository Name (default: crm-app): crm-app

Authenticating with AWS ECR...
Login Succeeded

Checking if ECR repository exists...
Repository exists

========================================
Building Docker image: 123456789012.dkr.ecr.us-east-1.amazonaws.com/crm-app:v1-0-0
========================================
...
Successfully built abc123def456
Successfully tagged 123456789012.dkr.ecr.us-east-1.amazonaws.com/crm-app:v1-0-0

========================================
Pushing image to registry
========================================
...
v1-0-0: digest: sha256:abc...def size: 2841

========================================
SUCCESS!
========================================
Image: 123456789012.dkr.ecr.us-east-1.amazonaws.com/crm-app:v1-0-0
Tag: v1-0-0

Next steps:
1. Run deploy-image.sh to deploy to AWS ECS
2. Use this image URI in your ECS task definition
```

---

## AWS ECS Fargate Prerequisites

### 1. VPC Configuration

**Requirements:**
- VPC with at least 2 subnets in different availability zones
- Subnets must have internet access (via Internet Gateway or NAT Gateway)
- DNS resolution enabled

**Create VPC (if needed):**

```bash
aws ec2 create-vpc --cidr-block 10.0.0.0/16 --region us-east-1
```

### 2. Security Group Configuration

**Create Security Group:**

```bash
aws ec2 create-security-group \
    --group-name crm-app-sg \
    --description "Security group for CRM application" \
    --vpc-id vpc-xxxxxxxxx \
    --region us-east-1
```

**Add Inbound Rules:**

```bash
# Allow HTTP traffic on port 8080 from ALB
aws ec2 authorize-security-group-ingress \
    --group-id sg-xxxxxxxxx \
    --protocol tcp \
    --port 8080 \
    --source-group sg-alb-xxxxxxxxx \
    --region us-east-1

# Allow MySQL traffic (if using RDS in same VPC)
aws ec2 authorize-security-group-ingress \
    --group-id sg-xxxxxxxxx \
    --protocol tcp \
    --port 3306 \
    --source-group sg-rds-xxxxxxxxx \
    --region us-east-1
```

### 3. IAM Roles

#### ECS Task Execution Role

This role allows ECS to pull images from ECR and write logs to CloudWatch.

**Create Role:**

```bash
aws iam create-role \
    --role-name ecsTaskExecutionRole \
    --assume-role-policy-document file://ecs-task-execution-trust-policy.json
```

**Trust Policy (ecs-task-execution-trust-policy.json):**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

**Attach Policies:**

```bash
aws iam attach-role-policy \
    --role-name ecsTaskExecutionRole \
    --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
```

#### ECS Task Role (Optional)

This role provides permissions to the application itself (e.g., to access S3, DynamoDB).

```bash
aws iam create-role \
    --role-name ecsTaskRole \
    --assume-role-policy-document file://ecs-task-execution-trust-policy.json

# Attach policies as needed for your application
aws iam attach-role-policy \
    --role-name ecsTaskRole \
    --policy-arn arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess
```

### 4. CloudWatch Log Group

**Create Log Group:**

```bash
aws logs create-log-group \
    --log-group-name /ecs/crm-app \
    --region us-east-1
```

**Set Retention:**

```bash
aws logs put-retention-policy \
    --log-group-name /ecs/crm-app \
    --retention-in-days 7 \
    --region us-east-1
```

---

## ECS Task Definition Explained

### Task Definition Structure

The task definition (`ecs/task-definition.json`) defines how your container should run on ECS Fargate.

**Key Components:**

#### 1. Launch Type Configuration

```json
{
  "family": "crm-app-task",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024"
}
```

- **family:** Task definition name
- **networkMode:** Must be `awsvpc` for Fargate
- **requiresCompatibilities:** Must include `FARGATE`
- **cpu/memory:** Must use valid Fargate combinations

#### 2. Valid CPU/Memory Combinations

| CPU | Memory Options (MB) |
|-----|---------------------|
| 256 | 512, 1024, 2048 |
| 512 | 1024, 2048, 3072, 4096 |
| 1024 | 2048-8192 (increments of 1024) |
| 2048 | 4096-16384 (increments of 1024) |
| 4096 | 8192-30720 (increments of 1024) |

**Default (recommended for this app):** cpu: `512`, memory: `1024`

#### 3. IAM Roles

```json
{
  "executionRoleArn": "arn:aws:iam::{{ACCOUNT_ID}}:role/ecsTaskExecutionRole",
  "taskRoleArn": "arn:aws:iam::{{ACCOUNT_ID}}:role/ecsTaskRole"
}
```

- **executionRoleArn:** Required for ECS to pull images and write logs
- **taskRoleArn:** Optional, provides AWS permissions to the application

#### 4. Container Definitions

```json
{
  "containerDefinitions": [
    {
      "name": "crm-app",
      "image": "{{IMAGE_URI}}",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "JAVA_OPTS",
          "value": "-Xmx512m -Xms256m -XX:+UseContainerSupport"
        },
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "production"
        },
        {
          "name": "DB_HOST",
          "value": "your-rds-endpoint.amazonaws.com"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/crm-app",
          "awslogs-region": "{{AWS_REGION}}",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
}
```

**Environment Variables:**
- `JAVA_OPTS`: JVM memory and container settings
- `SPRING_PROFILES_ACTIVE`: Spring Boot profile (production, staging, etc.)
- `DB_HOST`, `DB_PORT`, `DB_NAME`: Database connection details
- `DB_USER`, `DB_PASSWORD`: Database credentials (use AWS Secrets Manager in production)

---

## ECS Service Configuration

### Service Definition Structure

The service definition (`ecs/service-definition.json`) manages how tasks are deployed and scaled.

**Key Components:**

#### 1. Basic Configuration

```json
{
  "serviceName": "crm-app-service",
  "cluster": "{{CLUSTER_NAME}}",
  "taskDefinition": "crm-app-task",
  "desiredCount": 2,
  "launchType": "FARGATE"
}
```

- **desiredCount:** Number of task instances to run (2 for high availability)
- **launchType:** Must be `FARGATE`

#### 2. Network Configuration

```json
{
  "networkConfiguration": {
    "awsvpcConfiguration": {
      "subnets": ["{{SUBNET_1}}", "{{SUBNET_2}}"],
      "securityGroups": ["{{SECURITY_GROUP}}"],
      "assignPublicIp": "ENABLED"
    }
  }
}
```

- **subnets:** At least 2 subnets in different AZs
- **securityGroups:** Security group allowing inbound traffic on port 8080
- **assignPublicIp:** `ENABLED` if using public subnets, `DISABLED` for private subnets with NAT

#### 3. Load Balancer Configuration

```json
{
  "loadBalancers": [
    {
      "targetGroupArn": "{{TARGET_GROUP_ARN}}",
      "containerName": "crm-app",
      "containerPort": 8080
    }
  ],
  "healthCheckGracePeriodSeconds": 300
}
```

- **targetGroupArn:** ALB target group (automatically created by deploy script)
- **healthCheckGracePeriodSeconds:** Wait time before health checks start (important for JVM startup)

#### 4. Deployment Configuration

```json
{
  "deploymentConfiguration": {
    "maximumPercent": 200,
    "minimumHealthyPercent": 50,
    "deploymentCircuitBreaker": {
      "enable": true,
      "rollback": true
    }
  }
}
```

- **maximumPercent:** Maximum number of tasks during deployment (200 = 2x desired count)
- **minimumHealthyPercent:** Minimum healthy tasks during deployment
- **deploymentCircuitBreaker:** Automatic rollback on deployment failures

---

## AWS ECS Fargate Deployment

### Step-by-Step Deployment

#### 1. Prepare Deployment Files

Ensure you have:
- Built and pushed Docker image to ECR
- Task definition JSON file (`ecs/task-definition.json`)
- Service definition JSON file (`ecs/service-definition.json`)

#### 2. Run Deployment Script

**Linux/macOS:**

```bash
cd scripts
chmod +x deploy-image.sh
./deploy-image.sh
```

**Windows:**

```cmd
cd scripts
deploy-image.bat
```

#### 3. Provide Configuration Details

The script will prompt for:

1. **AWS Region** (e.g., `us-east-1`)
2. **ECS Cluster Name** (e.g., `crm-cluster`)
3. **VPC ID** (e.g., `vpc-0123456789abcdef`)
4. **Subnet IDs** (comma-separated, e.g., `subnet-abc123,subnet-def456`)
5. **Security Group ID** (e.g., `sg-0123456789abcdef`)
6. **ECR Image URI** (e.g., `123456789012.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest`)
7. **Load Balancer** (y/n)

#### 4. Deployment Process

The script will:

1. ✅ Retrieve AWS Account ID
2. ✅ Check/create ECS cluster
3. ✅ Create Application Load Balancer (if requested)
4. ✅ Create Target Group with health checks (`/appinfo/health`)
5. ✅ Register task definition
6. ✅ Create or update ECS service
7. ✅ Wait for service to stabilize
8. ✅ Display deployment status

#### 5. Verify Deployment

**Check Service Status:**

```bash
aws ecs describe-services \
    --cluster crm-cluster \
    --services crm-app-service \
    --region us-east-1
```

**Check Running Tasks:**

```bash
aws ecs list-tasks \
    --cluster crm-cluster \
    --service-name crm-app-service \
    --region us-east-1
```

**Access Application:**

If load balancer was created, the script will output the DNS name:

```
Load Balancer DNS: http://crm-app-alb-123456789.us-east-1.elb.amazonaws.com
```

Access the application:
- **Application:** http://[ALB-DNS-NAME]
- **Health Check:** http://[ALB-DNS-NAME]/appinfo/health

---

## Configuration Management

### Environment Variables

**Database Configuration:**

```json
{
  "name": "DB_HOST",
  "value": "your-rds-endpoint.rds.amazonaws.com"
},
{
  "name": "DB_PORT",
  "value": "3306"
},
{
  "name": "DB_NAME",
  "value": "crm"
},
{
  "name": "DB_USER",
  "value": "admin"
},
{
  "name": "DB_PASSWORD",
  "value": "changeme"
}
```

**⚠️ Security Warning:** Do NOT hardcode passwords in task definitions. Use AWS Secrets Manager.

### Using AWS Secrets Manager

**Create Secret:**

```bash
aws secretsmanager create-secret \
    --name crm-app/db-password \
    --secret-string "your-secure-password" \
    --region us-east-1
```

**Reference in Task Definition:**

```json
{
  "secrets": [
    {
      "name": "DB_PASSWORD",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:crm-app/db-password-AbCdEf"
    }
  ]
}
```

**Required IAM Permission:**

Add to task execution role:

```json
{
  "Effect": "Allow",
  "Action": [
    "secretsmanager:GetSecretValue"
  ],
  "Resource": "arn:aws:secretsmanager:us-east-1:123456789012:secret:crm-app/*"
}
```

### Spring Boot Profiles

Set different profiles for different environments:

```json
{
  "name": "SPRING_PROFILES_ACTIVE",
  "value": "production"
}
```

Create profile-specific configuration files:
- `application-production.properties`
- `application-staging.properties`
- `application-development.properties`

---

## Monitoring and Logging

### CloudWatch Logs

**View Logs:**

```bash
aws logs tail /ecs/crm-app --follow --region us-east-1
```

**Filter Logs:**

```bash
aws logs filter-log-events \
    --log-group-name /ecs/crm-app \
    --filter-pattern "ERROR" \
    --region us-east-1
```

### CloudWatch Metrics

**ECS Service Metrics:**
- CPUUtilization
- MemoryUtilization
- DesiredTaskCount
- RunningTaskCount

**Create CloudWatch Dashboard:**

```bash
aws cloudwatch put-dashboard \
    --dashboard-name crm-app-dashboard \
    --dashboard-body file://dashboard.json \
    --region us-east-1
```

### Application Performance Monitoring

**Spring Boot Actuator Endpoints:**

- `/appinfo/health` - Application health status
- `/appinfo/info` - Application information
- `/appinfo/metrics` - Application metrics

**Access via Load Balancer:**

```bash
curl http://[ALB-DNS-NAME]/appinfo/health
```

---

## Troubleshooting

### Common Issues

#### 1. Task Fails to Start

**Symptom:** Tasks continuously stop and restart

**Diagnosis:**

```bash
aws ecs describe-tasks \
    --cluster crm-cluster \
    --tasks [TASK-ARN] \
    --region us-east-1
```

**Common Causes:**
- Invalid CPU/memory combination
- Image pull failure (check ECR permissions)
- Application crashes on startup (check CloudWatch logs)
- Database connection failure

**Solutions:**

```bash
# Check CloudWatch logs
aws logs tail /ecs/crm-app --follow --region us-east-1

# Verify task execution role has ECR permissions
aws iam list-attached-role-policies --role-name ecsTaskExecutionRole

# Test database connectivity from VPC
telnet your-rds-endpoint.rds.amazonaws.com 3306
```

#### 2. Load Balancer Health Checks Failing

**Symptom:** Target group shows unhealthy targets

**Diagnosis:**

```bash
aws elbv2 describe-target-health \
    --target-group-arn [TARGET-GROUP-ARN] \
    --region us-east-1
```

**Common Causes:**
- Health check path incorrect
- Security group not allowing ALB traffic
- Application not responding on port 8080
- JVM taking too long to start (increase `healthCheckGracePeriodSeconds`)

**Solutions:**

```bash
# Verify health endpoint
curl http://[TASK-IP]:8080/appinfo/health

# Check security group rules
aws ec2 describe-security-groups --group-ids [SG-ID]

# Increase health check grace period
aws ecs update-service \
    --cluster crm-cluster \
    --service crm-app-service \
    --health-check-grace-period-seconds 300 \
    --region us-east-1
```

#### 3. Service Not Stabilizing

**Symptom:** Deployment hangs on "Waiting for service to stabilize"

**Diagnosis:**

```bash
aws ecs describe-services \
    --cluster crm-cluster \
    --services crm-app-service \
    --region us-east-1 \
    --query 'services[0].events[:5]'
```

**Common Causes:**
- Insufficient resources in subnets
- Task fails health checks
- Deployment circuit breaker triggered

**Solutions:**

```bash
# Check service events
aws ecs describe-services \
    --cluster crm-cluster \
    --services crm-app-service \
    --region us-east-1

# Force new deployment
aws ecs update-service \
    --cluster crm-cluster \
    --service crm-app-service \
    --force-new-deployment \
    --region us-east-1
```

#### 4. Out of Memory Errors

**Symptom:** Tasks stop with exit code 137 or OOMKilled

**Diagnosis:**

Check CloudWatch logs for:
```
java.lang.OutOfMemoryError: Java heap space
```

**Solutions:**

1. **Increase task memory:**
   - Update task definition to use higher memory (e.g., 2048 MB)

2. **Tune JVM settings:**
   ```json
   {
     "name": "JAVA_OPTS",
     "value": "-Xmx1024m -Xms512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
   }
   ```

3. **Enable heap dumps:**
   ```json
   {
     "name": "JAVA_OPTS",
     "value": "-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs"
   }
   ```

#### 5. Network Connectivity Issues

**Symptom:** Tasks cannot connect to external services

**Common Causes:**
- Subnets don't have internet access
- Security groups blocking outbound traffic
- NAT Gateway issues

**Solutions:**

```bash
# Verify subnet route tables
aws ec2 describe-route-tables --filters "Name=association.subnet-id,Values=[SUBNET-ID]"

# Check security group outbound rules
aws ec2 describe-security-groups --group-ids [SG-ID] --query 'SecurityGroups[0].IpPermissionsEgress'

# Test from task (exec into running container)
aws ecs execute-command \
    --cluster crm-cluster \
    --task [TASK-ID] \
    --container crm-app \
    --interactive \
    --command "/bin/sh"
```

---

## Scaling and Management

### Manual Scaling

**Update Desired Count:**

```bash
aws ecs update-service \
    --cluster crm-cluster \
    --service crm-app-service \
    --desired-count 4 \
    --region us-east-1
```

### Auto Scaling

#### 1. Register Scalable Target

```bash
aws application-autoscaling register-scalable-target \
    --service-namespace ecs \
    --resource-id service/crm-cluster/crm-app-service \
    --scalable-dimension ecs:service:DesiredCount \
    --min-capacity 2 \
    --max-capacity 10 \
    --region us-east-1
```

#### 2. Create Scaling Policy (CPU-based)

```bash
aws application-autoscaling put-scaling-policy \
    --service-namespace ecs \
    --resource-id service/crm-cluster/crm-app-service \
    --scalable-dimension ecs:service:DesiredCount \
    --policy-name cpu-scaling-policy \
    --policy-type TargetTrackingScaling \
    --target-tracking-scaling-policy-configuration file://scaling-policy.json \
    --region us-east-1
```

**scaling-policy.json:**

```json
{
  "TargetValue": 70.0,
  "PredefinedMetricSpecification": {
    "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
  },
  "ScaleInCooldown": 300,
  "ScaleOutCooldown": 60
}
```

### Blue/Green Deployments

**Using AWS CodeDeploy:**

1. Create CodeDeploy application:
   ```bash
   aws deploy create-application \
       --application-name crm-app \
       --compute-platform ECS
   ```

2. Create deployment group with blue/green configuration
3. Deploy new task definition revision
4. CodeDeploy handles traffic shifting

---

## Security Best Practices

### 1. Use AWS Secrets Manager

✅ **DO:**
- Store database passwords in Secrets Manager
- Store API keys and tokens in Secrets Manager
- Rotate secrets regularly

❌ **DON'T:**
- Hardcode passwords in task definitions
- Store secrets in environment variables
- Commit secrets to version control

### 2. IAM Least Privilege

✅ **DO:**
- Use separate task and execution roles
- Grant only required permissions
- Use condition keys to restrict access

❌ **DON'T:**
- Use admin policies
- Share roles across different applications
- Use root AWS account credentials

### 3. Network Security

✅ **DO:**
- Use private subnets with NAT Gateway
- Restrict security group rules to minimum required
- Enable VPC Flow Logs
- Use AWS WAF with Application Load Balancer

❌ **DON'T:**
- Expose ECS tasks directly to internet
- Use 0.0.0.0/0 for inbound rules (except ALB)
- Disable encryption in transit

### 4. Container Security

✅ **DO:**
- Run containers as non-root user
- Scan images for vulnerabilities (ECR scanning)
- Use minimal base images (alpine)
- Keep base images updated

❌ **DON'T:**
- Run as root user
- Use `latest` tag in production
- Include development tools in production images

### 5. Logging and Monitoring

✅ **DO:**
- Enable CloudWatch Container Insights
- Set log retention policies
- Monitor for security events
- Enable AWS GuardDuty

❌ **DON'T:**
- Disable logging
- Log sensitive information (passwords, tokens)
- Ignore security alerts

---

## Technology-Specific Notes

### Spring Boot 1.5.10 Considerations

**1. Database Connection Pool:**

The application uses Hikari connection pool. Configure for containerized environment:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

**2. Hibernate DDL:**

⚠️ **WARNING:** Default configuration uses `spring.jpa.hibernate.ddl-auto=create-drop`

This will **DROP AND RECREATE** the database schema on every restart!

**For Production:**
```properties
spring.jpa.hibernate.ddl-auto=validate
```

**Use Database Migration Tools:**
- Flyway
- Liquibase

**3. JVM Tuning for Containers:**

```bash
JAVA_OPTS="
  -Xmx512m
  -Xms256m
  -XX:+UseContainerSupport
  -XX:MaxRAMPercentage=75.0
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -Djava.security.egd=file:/dev/./urandom
"
```

**4. Spring Boot Actuator Security:**

Default configuration disables security: `management.security.enabled=false`

**For Production:** Enable security and use HTTP Basic authentication

```properties
management.security.enabled=true
security.user.name=admin
security.user.password=${ACTUATOR_PASSWORD}
```

**5. Thymeleaf Template Caching:**

Default: `spring.thymeleaf.cache=false` (development mode)

**For Production:**
```properties
spring.thymeleaf.cache=true
```

**6. Static Resource Handling:**

For better performance, consider using CloudFront CDN for static assets (CSS, JS, images).

---

## Additional Resources

### AWS Documentation
- [ECS Fargate Documentation](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html)
- [Task Definition Parameters](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task_definition_parameters.html)
- [Service Definition Parameters](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/service_definition_parameters.html)
- [CloudWatch Container Insights](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/ContainerInsights.html)

### Spring Boot Resources
- [Spring Boot 1.5.x Documentation](https://docs.spring.io/spring-boot/docs/1.5.x/reference/html/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/1.5.x/reference/html/production-ready.html)
- [Spring Security Reference](https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/)

### Docker Resources
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Multi-stage Builds](https://docs.docker.com/develop/develop-images/multistage-build/)

---

## Support and Troubleshooting

For issues or questions:

1. Check CloudWatch logs: `/ecs/crm-app`
2. Review ECS service events
3. Verify security group and network configuration
4. Check IAM role permissions
5. Review application configuration

**Deployment Health Checklist:**

- [ ] Docker image built and pushed to ECR
- [ ] VPC and subnets configured
- [ ] Security groups allow required traffic
- [ ] IAM roles created with correct policies
- [ ] RDS MySQL database accessible from ECS tasks
- [ ] Environment variables configured correctly
- [ ] CloudWatch log group created
- [ ] Load balancer and target group configured (if needed)
- [ ] Task definition registered
- [ ] Service created and tasks running
- [ ] Health checks passing
- [ ] Application accessible via load balancer

---

**End of Deployment Guide**