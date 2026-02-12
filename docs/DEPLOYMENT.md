# TestjavaCont3 - AWS ECS Fargate Deployment Guide

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Local Development Setup](#local-development-setup)
4. [Docker Build and Push](#docker-build-and-push)
5. [AWS ECS Fargate Prerequisites](#aws-ecs-fargate-prerequisites)
6. [ECS Task Definition Explained](#ecs-task-definition-explained)
7. [ECS Service Configuration](#ecs-service-configuration)
8. [AWS ECS Fargate Deployment](#aws-ecs-fargate-deployment)
9. [Troubleshooting](#troubleshooting)
10. [Scaling and Management](#scaling-and-management)
11. [Security Considerations](#security-considerations)

---

## Overview

This guide provides comprehensive instructions for deploying the TestjavaCont3 Spring Boot application to AWS ECS Fargate. The application is containerized using Docker and deployed as a serverless container service.

**Application Details:**
- **Technology Stack**: Java 8, Spring Boot, Maven
- **Application Port**: 8080
- **Health Check**: `/actuator/health`
- **Container Registry**: AWS ECR or Docker Hub
- **Target Platform**: AWS ECS Fargate

---

## Prerequisites

### Required Software

1. **Docker** (version 20.10 or later)
   - Download: https://www.docker.com/products/docker-desktop
   - Verify: `docker --version`

2. **AWS CLI** (version 2.x)
   - Download: https://aws.amazon.com/cli/
   - Verify: `aws --version`
   - Configure: `aws configure`

3. **Git** (for cloning repository)
   - Verify: `git --version`

4. **Java 8 JDK** (for local development)
   - Verify: `java -version`

5. **Maven** (version 3.6 or later)
   - Verify: `mvn --version`

### AWS Account Requirements

- Active AWS account with appropriate permissions
- IAM user with permissions for:
  - ECS (create/update clusters, services, task definitions)
  - ECR (create repositories, push images)
  - EC2 (create/manage VPC, subnets, security groups)
  - IAM (create/assign roles)
  - CloudWatch Logs (create log groups, write logs)
  - Elastic Load Balancing (optional, for ALB/NLB)

---

## Local Development Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd TestjavaCont3
```

### 2. Build the Application Locally

```bash
mvn clean package -DskipTests
```

The JAR file will be created in `target/` directory.

### 3. Run with Docker Compose (Local Testing)

```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`

Test the health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

Stop the application:
```bash
docker-compose down
```

---

## Docker Build and Push

### Option 1: Using Build Script (Recommended)

**Linux/macOS:**
```bash
chmod +x scripts/build-push.sh
./scripts/build-push.sh
```

**Windows:**
```cmd
scripts\build-push.bat
```

The script will:
1. Prompt for registry type (AWS ECR or Docker Hub)
2. Request registry credentials and configuration
3. Build the Docker image
4. Authenticate with the selected registry
5. Push the image to the registry

### Option 2: Manual Build and Push

#### AWS ECR:

```bash
# Set variables
AWS_REGION="us-east-1"
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ECR_REPO="testjavacont3"
IMAGE_TAG="latest"

# Create ECR repository (if not exists)
aws ecr create-repository --repository-name $ECR_REPO --region $AWS_REGION || true

# Authenticate Docker to ECR
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Build image
docker build -t $ECR_REPO:$IMAGE_TAG .

# Tag image
docker tag $ECR_REPO:$IMAGE_TAG $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:$IMAGE_TAG

# Push image
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:$IMAGE_TAG
```

#### Docker Hub:

```bash
# Set variables
DOCKER_USERNAME="your-username"
IMAGE_TAG="latest"

# Login to Docker Hub
docker login -u $DOCKER_USERNAME

# Build image
docker build -t testjavacont3:$IMAGE_TAG .

# Tag image
docker tag testjavacont3:$IMAGE_TAG $DOCKER_USERNAME/testjavacont3:$IMAGE_TAG

# Push image
docker push $DOCKER_USERNAME/testjavacont3:$IMAGE_TAG
```

---

## AWS ECS Fargate Prerequisites

### 1. VPC and Networking

You need a VPC with:
- At least 2 subnets (preferably in different Availability Zones for high availability)
- Internet Gateway attached (for public subnets)
- Route table configured for internet access

**Create VPC (if needed):**
```bash
aws ec2 create-vpc --cidr-block 10.0.0.0/16 --region us-east-1
```

**Note existing VPC details:**
```bash
aws ec2 describe-vpcs --region us-east-1
aws ec2 describe-subnets --region us-east-1
```

### 2. Security Group

Create a security group that allows:
- Inbound traffic on port 8080 (application port)
- Outbound traffic to the internet (for pulling images, etc.)

**Create Security Group:**
```bash
VPC_ID="vpc-xxxxxxxxx"
aws ec2 create-security-group \
  --group-name testjavacont3-sg \
  --description "Security group for TestjavaCont3 ECS tasks" \
  --vpc-id $VPC_ID \
  --region us-east-1

SG_ID="sg-xxxxxxxxx"  # From output above

# Allow inbound on port 8080
aws ec2 authorize-security-group-ingress \
  --group-id $SG_ID \
  --protocol tcp \
  --port 8080 \
  --cidr 0.0.0.0/0 \
  --region us-east-1
```

### 3. IAM Roles

#### ECS Task Execution Role

This role allows ECS to pull images from ECR and write logs to CloudWatch.

**Create role (if not exists):**
```bash
aws iam create-role \
  --role-name ecsTaskExecutionRole \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [{
      "Effect": "Allow",
      "Principal": {"Service": "ecs-tasks.amazonaws.com"},
      "Action": "sts:AssumeRole"
    }]
  }'

# Attach policy
aws iam attach-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
```

#### ECS Task Role (Optional)

This role grants permissions to the application running in the container (e.g., access to S3, DynamoDB).

```bash
aws iam create-role \
  --role-name ecsTaskRole \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [{
      "Effect": "Allow",
      "Principal": {"Service": "ecs-tasks.amazonaws.com"},
      "Action": "sts:AssumeRole"
    }]
  }'

# Attach policies as needed for your application
```

### 4. CloudWatch Log Group

Create a log group for application logs:

```bash
aws logs create-log-group --log-group-name /ecs/testjavacont3 --region us-east-1
```

---

## ECS Task Definition Explained

The task definition (`ecs/task-definition.json`) defines how your container should run.

**Key Configuration:**

```json
{
  "family": "testjavacont3-task",
  "networkMode": "awsvpc",          // Required for Fargate
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",                     // .5 vCPU
  "memory": "1024",                 // 1 GB RAM
  "executionRoleArn": "arn:aws:iam::ACCOUNT_ID:role/ecsTaskExecutionRole",
  "containerDefinitions": [{
    "name": "testjavacont3",
    "image": "IMAGE_URI",
    "portMappings": [{"containerPort": 8080}],
    "environment": [
      {"name": "JAVA_OPTS", "value": "-Xmx768m -Xms384m"},
      {"name": "SPRING_PROFILES_ACTIVE", "value": "production"}
    ],
    "logConfiguration": {
      "logDriver": "awslogs",
      "options": {
        "awslogs-group": "/ecs/testjavacont3",
        "awslogs-region": "us-east-1",
        "awslogs-stream-prefix": "ecs"
      }
    }
  }]
}
```

**Valid Fargate CPU/Memory Combinations:**

| CPU (vCPU) | Memory (MB) |
|------------|-------------|
| 256 (.25)  | 512, 1024, 2048 |
| 512 (.5)   | 1024, 2048, 3072, 4096 |
| 1024 (1)   | 2048-8192 (increments of 1024) |
| 2048 (2)   | 4096-16384 (increments of 1024) |
| 4096 (4)   | 8192-30720 (increments of 1024) |

---

## ECS Service Configuration

The service definition (`ecs/service-definition.json`) manages the deployment and scaling of tasks.

**Key Configuration:**

```json
{
  "serviceName": "testjavacont3-service",
  "cluster": "CLUSTER_NAME",
  "taskDefinition": "testjavacont3-task",
  "desiredCount": 2,                // Number of tasks to run
  "launchType": "FARGATE",
  "networkConfiguration": {
    "awsvpcConfiguration": {
      "subnets": ["subnet-xxx", "subnet-yyy"],
      "securityGroups": ["sg-xxx"],
      "assignPublicIp": "ENABLED"   // Required for pulling images from ECR
    }
  },
  "loadBalancers": [{
    "targetGroupArn": "arn:aws:elasticloadbalancing:...",
    "containerName": "testjavacont3",
    "containerPort": 8080
  }],
  "healthCheckGracePeriodSeconds": 300  // Time before health checks start
}
```

---

## AWS ECS Fargate Deployment

### Using Deployment Script (Recommended)

**Linux/macOS:**
```bash
chmod +x scripts/deploy-image.sh
./scripts/deploy-image.sh
```

**Windows:**
```cmd
scripts\deploy-image.bat
```

The script will:
1. Prompt for AWS region and ECS cluster name
2. Request VPC, subnet, and security group configuration
3. Ask for the ECR image URI
4. Optionally create an Application Load Balancer and Target Group
5. Register the task definition
6. Create or update the ECS service
7. Wait for service stability
8. Display deployment status and logs information

### Manual Deployment

#### 1. Register Task Definition

```bash
aws ecs register-task-definition \
  --cli-input-json file://ecs/task-definition.json \
  --region us-east-1
```

#### 2. Create ECS Cluster

```bash
aws ecs create-cluster \
  --cluster-name testjavacont3-cluster \
  --region us-east-1
```

#### 3. Create Service

```bash
aws ecs create-service \
  --cluster testjavacont3-cluster \
  --service-name testjavacont3-service \
  --cli-input-json file://ecs/service-definition.json \
  --region us-east-1
```

#### 4. Verify Deployment

```bash
aws ecs describe-services \
  --cluster testjavacont3-cluster \
  --services testjavacont3-service \
  --region us-east-1
```

#### 5. View Logs

```bash
aws logs tail /ecs/testjavacont3 --follow --region us-east-1
```

---

## Troubleshooting

### Common Issues

#### 1. Task Fails to Start

**Symptom:** Tasks transition to STOPPED state immediately

**Solutions:**
- Check CloudWatch logs: `aws logs tail /ecs/testjavacont3 --region us-east-1`
- Verify image URI is correct and accessible
- Ensure execution role has permissions to pull from ECR
- Check if CPU/memory combination is valid

#### 2. Cannot Pull Image from ECR

**Symptom:** Error "CannotPullContainerError"

**Solutions:**
- Verify ECR repository exists: `aws ecr describe-repositories --region us-east-1`
- Check execution role has `AmazonECSTaskExecutionRolePolicy` attached
- Ensure subnets have internet access (via Internet Gateway or NAT Gateway)
- Verify `assignPublicIp: ENABLED` in service definition

#### 3. Health Check Failures

**Symptom:** Tasks start but fail health checks

**Solutions:**
- Verify application is listening on port 8080
- Check health endpoint is accessible: `/actuator/health`
- Increase `healthCheckGracePeriodSeconds` to allow for longer startup time
- Review application logs for startup errors

#### 4. Network Connectivity Issues

**Symptom:** Cannot access application or tasks cannot reach external services

**Solutions:**
- Verify security group allows inbound traffic on port 8080
- Check subnets have proper routing to Internet Gateway
- Ensure target group health checks are configured correctly
- Verify Load Balancer listener is forwarding to correct target group

#### 5. Out of Memory Errors

**Symptom:** Tasks crash with OOM errors

**Solutions:**
- Increase task memory in task definition
- Adjust JVM heap size in `JAVA_OPTS` environment variable
- Use valid CPU/memory combination (e.g., CPU: 1024, Memory: 2048)

### Viewing Logs

**CloudWatch Logs:**
```bash
# Tail logs
aws logs tail /ecs/testjavacont3 --follow --region us-east-1

# View specific time range
aws logs filter-log-events \
  --log-group-name /ecs/testjavacont3 \
  --start-time $(date -d '1 hour ago' +%s)000 \
  --region us-east-1
```

**ECS Task Logs:**
```bash
# List tasks
aws ecs list-tasks \
  --cluster testjavacont3-cluster \
  --service-name testjavacont3-service \
  --region us-east-1

# Describe specific task
aws ecs describe-tasks \
  --cluster testjavacont3-cluster \
  --tasks <task-arn> \
  --region us-east-1
```

---

## Scaling and Management

### Manual Scaling

```bash
# Scale to 5 tasks
aws ecs update-service \
  --cluster testjavacont3-cluster \
  --service testjavacont3-service \
  --desired-count 5 \
  --region us-east-1
```

### Auto Scaling

#### 1. Register Scalable Target

```bash
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/testjavacont3-cluster/testjavacont3-service \
  --min-capacity 2 \
  --max-capacity 10 \
  --region us-east-1
```

#### 2. Create Scaling Policy

**CPU-based scaling:**
```bash
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/testjavacont3-cluster/testjavacont3-service \
  --policy-name cpu-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration '{
    "TargetValue": 70.0,
    "PredefinedMetricSpecification": {
      "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
    },
    "ScaleInCooldown": 300,
    "ScaleOutCooldown": 60
  }' \
  --region us-east-1
```

### Blue/Green Deployment

For zero-downtime deployments, use AWS CodeDeploy with ECS:

1. Create CodeDeploy application and deployment group
2. Configure deployment configuration (e.g., `CodeDeployDefault.ECSAllAtOnce`)
3. Update task definition and trigger deployment

### Rolling Updates

```bash
# Update service with new task definition
aws ecs update-service \
  --cluster testjavacont3-cluster \
  --service testjavacont3-service \
  --task-definition testjavacont3-task:2 \
  --force-new-deployment \
  --region us-east-1
```

---

## Security Considerations

### 1. Container Security

- **Run as non-root user**: Dockerfile creates and uses `appuser`
- **Minimal base image**: Uses `eclipse-temurin:8-jre-alpine` (smaller attack surface)
- **No unnecessary packages**: Runtime image contains only JRE
- **Regular updates**: Keep base images updated with security patches

### 2. Network Security

- **Security Groups**: Restrict inbound traffic to necessary ports only
- **Private Subnets**: Consider using private subnets with NAT Gateway for production
- **VPC Flow Logs**: Enable for network traffic monitoring

### 3. Secrets Management

- **Never hardcode secrets**: Use AWS Secrets Manager or Parameter Store
- **Environment variables**: For sensitive data, use `secrets` instead of `environment`

**Example:**
```json
"secrets": [
  {
    "name": "DB_PASSWORD",
    "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789:secret:db-password"
  }
]
```

### 4. IAM Best Practices

- **Principle of least privilege**: Grant only necessary permissions
- **Separate roles**: Use different roles for execution and task
- **Audit policies**: Regularly review IAM policies

### 5. Image Security

- **Scan images**: Use ECR image scanning for vulnerabilities
- **Private registries**: Use ECR instead of public Docker Hub for production
- **Image signing**: Consider using Docker Content Trust

### 6. Logging and Monitoring

- **CloudWatch Logs**: Centralized logging for all tasks
- **CloudWatch Alarms**: Set up alarms for errors, high CPU, memory usage
- **AWS X-Ray**: Enable distributed tracing for Spring Boot applications

---

## Additional Resources

- [AWS ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [AWS Fargate Documentation](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html)
- [Spring Boot on AWS](https://spring.io/guides/gs/spring-boot-aws/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

---

## Support

For issues or questions:
1. Check CloudWatch logs for error messages
2. Review this troubleshooting guide
3. Consult AWS ECS documentation
4. Contact your DevOps team or AWS support

---

**Document Version:** 1.0  
**Last Updated:** 2026-02-12  
**Deployment Platform:** AWS ECS Fargate