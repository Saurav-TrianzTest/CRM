# CRM Application - AWS ECS Fargate Deployment Guide

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Project Structure](#project-structure)
4. [Local Development Setup](#local-development-setup)
5. [Building and Pushing Docker Images](#building-and-pushing-docker-images)
6. [AWS ECS Fargate Prerequisites](#aws-ecs-fargate-prerequisites)
7. [ECS Fargate Setup](#ecs-fargate-setup)
8. [ECS Task Definition](#ecs-task-definition)
9. [ECS Service Configuration](#ecs-service-configuration)
10. [Deploying to AWS ECS Fargate](#deploying-to-aws-ecs-fargate)
11. [Monitoring and Logging](#monitoring-and-logging)
12. [Troubleshooting](#troubleshooting)
13. [Security Best Practices](#security-best-practices)
14. [Scaling and Management](#scaling-and-management)

---

## Overview

This deployment guide covers the containerization and deployment of a Spring Boot 1.5.10 CRM application to AWS ECS Fargate. The application is built with Java 8 and uses Maven as the build tool.

**Technology Stack:**
- Java 8
- Spring Boot 1.5.10.RELEASE
- Maven 3.9.4
- MySQL Database
- Spring Security
- Thymeleaf Template Engine
- Spring Boot Actuator

**Deployment Platform:**
- AWS ECS Fargate
- AWS Elastic Container Registry (ECR)
- AWS Application Load Balancer (ALB)
- AWS CloudWatch Logs

---

## Prerequisites

### Required Software

1. **Docker** (version 20.10 or later)
   - [Install Docker Desktop](https://www.docker.com/products/docker-desktop)
   - Verify installation: `docker --version`

2. **AWS CLI** (version 2.0 or later)
   - [Install AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
   - Configure credentials: `aws configure`
   - Verify installation: `aws --version`

3. **Git** (for version control)
   - [Install Git](https://git-scm.com/downloads)

### AWS Account Requirements

1. **AWS Account** with appropriate permissions
2. **IAM User** with the following permissions:
   - ECS Full Access
   - ECR Full Access
   - EC2 (for VPC, Security Groups, Load Balancers)
   - IAM (for task execution roles)
   - CloudWatch Logs

3. **AWS Resources:**
   - VPC with at least 2 subnets in different availability zones
   - Security group allowing inbound traffic on ports 80 and 8080
   - MySQL database (RDS or external)

---

## Project Structure

```
crm-comp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── crm/
│   │   │       ├── CrmApplication.java
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── controller/
│   │   │       └── ...
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   └── test/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── scripts/
│   ├── build-push.sh
│   ├── build-push.bat
│   ├── deploy-image.sh
│   └── deploy-image.bat
├── ecs/
│   ├── task-definition.json
│   └── service-definition.json
└── docs/
    └── DEPLOYMENT.md
```

---

## Local Development Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd crm-comp
```

### 2. Configure Application Properties

Update `src/main/resources/application.properties` with your local database configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crm?useSSL=false
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=validate
```

### 3. Build the Application Locally

```bash
mvn clean package
```

### 4. Run the Application

```bash
java -jar target/crm-0.0.1-SNAPSHOT.jar
```

Access the application at: `http://localhost:8080`

Health check endpoint: `http://localhost:8080/appinfo/health`

### 5. Using Docker Compose for Local Development

```bash
# Set environment variables
export DB_HOST=your-mysql-host
export DB_PORT=3306
export DB_NAME=crm
export DB_USERNAME=root
export DB_PASSWORD=password

# Start the application
docker-compose up --build
```

The application will be available at `http://localhost:8080`.

**Note:** Docker Compose runs only the application container. Ensure your MySQL database is running separately.

---

## Building and Pushing Docker Images

### Option 1: Using the Build Script (Linux/macOS)

```bash
cd scripts
chmod +x build-push.sh
./build-push.sh
```

The script will:
1. Prompt you to select a registry (AWS ECR or Docker Hub)
2. Request registry credentials and configuration
3. Build the Docker image
4. Authenticate with the selected registry
5. Push the image to the registry

### Option 2: Using the Build Script (Windows)

```cmd
cd scripts
build-push.bat
```

### Option 3: Manual Build and Push

#### For AWS ECR:

```bash
# Set variables
AWS_REGION=us-east-1
AWS_ACCOUNT_ID=123456789012
REPO_NAME=crm-comp
IMAGE_TAG=latest

# Authenticate with ECR
aws ecr get-login-password --region $AWS_REGION | \
  docker login --username AWS --password-stdin \
  $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Create repository (if not exists)
aws ecr create-repository --repository-name $REPO_NAME --region $AWS_REGION

# Build image
docker build -t crm-comp:$IMAGE_TAG .

# Tag image
docker tag crm-comp:$IMAGE_TAG \
  $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:$IMAGE_TAG

# Push image
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:$IMAGE_TAG
```

---

## AWS ECS Fargate Prerequisites

### 1. Create VPC and Subnets

If you don't have a VPC, create one with at least 2 public subnets:

```bash
aws ec2 create-vpc --cidr-block 10.0.0.0/16 --region us-east-1
```

### 2. Create Security Group

Create a security group that allows inbound traffic:

```bash
aws ec2 create-security-group \
  --group-name crm-sg \
  --description "Security group for CRM application" \
  --vpc-id <vpc-id> \
  --region us-east-1

# Allow HTTP traffic
aws ec2 authorize-security-group-ingress \
  --group-id <sg-id> \
  --protocol tcp \
  --port 80 \
  --cidr 0.0.0.0/0

# Allow application traffic
aws ec2 authorize-security-group-ingress \
  --group-id <sg-id> \
  --protocol tcp \
  --port 8080 \
  --cidr 0.0.0.0/0
```

### 3. Create IAM Roles

#### ECS Task Execution Role

Create a role that allows ECS to pull images from ECR and write logs to CloudWatch:

```bash
# Create trust policy file
cat > ecs-task-execution-trust-policy.json <<EOF
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
EOF

# Create role
aws iam create-role \
  --role-name ecsTaskExecutionRole \
  --assume-role-policy-document file://ecs-task-execution-trust-policy.json

# Attach managed policy
aws iam attach-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
```

#### ECS Task Role (Optional)

Create a role for application-level permissions:

```bash
aws iam create-role \
  --role-name ecsTaskRole \
  --assume-role-policy-document file://ecs-task-execution-trust-policy.json
```

### 4. Create MySQL Database

Set up an RDS MySQL instance or use an external MySQL database.

```bash
aws rds create-db-instance \
  --db-instance-identifier crm-db \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --master-username admin \
  --master-user-password <password> \
  --allocated-storage 20 \
  --vpc-security-group-ids <sg-id> \
  --publicly-accessible
```

---

## ECS Fargate Setup

### 1. Create ECS Cluster

```bash
aws ecs create-cluster --cluster-name crm-cluster --region us-east-1
```

### 2. Create CloudWatch Log Group

```bash
aws logs create-log-group --log-group-name /ecs/crm-comp --region us-east-1
```

---

## ECS Task Definition

The task definition (`ecs/task-definition.json`) defines how your container should run on ECS Fargate.

### Key Components:

1. **Launch Type Compatibility:**
   ```json
   "requiresCompatibilities": ["FARGATE"]
   ```

2. **Network Mode:**
   ```json
   "networkMode": "awsvpc"
   ```
   Fargate requires `awsvpc` network mode.

3. **CPU and Memory:**
   ```json
   "cpu": "512",
   "memory": "1024"
   ```
   Valid Fargate CPU/Memory combinations:
   - CPU: 512 (.5 vCPU) → Memory: 1024, 2048, 3072, 4096 MB
   - CPU: 1024 (1 vCPU) → Memory: 2048-8192 MB

4. **Execution Role:**
   ```json
   "executionRoleArn": "arn:aws:iam::{{ACCOUNT_ID}}:role/ecsTaskExecutionRole"
   ```

5. **Container Definition:**
   - Image URI from ECR
   - Port mappings (only containerPort for Fargate)
   - Environment variables for database connection
   - Health check configuration
   - CloudWatch logging

### Register Task Definition:

```bash
aws ecs register-task-definition \
  --cli-input-json file://ecs/task-definition.json \
  --region us-east-1
```

---

## ECS Service Configuration

The service definition (`ecs/service-definition.json`) manages running tasks and integrates with load balancers.

### Key Components:

1. **Launch Type:**
   ```json
   "launchType": "FARGATE"
   ```

2. **Network Configuration:**
   ```json
   "networkConfiguration": {
     "awsvpcConfiguration": {
       "subnets": ["subnet-xxx", "subnet-yyy"],
       "securityGroups": ["sg-xxx"],
       "assignPublicIp": "ENABLED"
     }
   }
   ```

3. **Desired Count:**
   Number of tasks to run (default: 2 for high availability)

4. **Load Balancer (Optional):**
   ```json
   "loadBalancers": [
     {
       "targetGroupArn": "arn:aws:elasticloadbalancing:...",
       "containerName": "crm-comp",
       "containerPort": 8080
     }
   ]
   ```

5. **Deployment Configuration:**
   - Rolling update strategy
   - Circuit breaker for automatic rollback

---

## Deploying to AWS ECS Fargate

### Using the Deployment Script (Linux/macOS)

```bash
cd scripts
chmod +x deploy-image.sh
./deploy-image.sh
```

The script will:
1. Prompt for AWS region and cluster name
2. Request network configuration (VPC, subnets, security groups)
3. Prompt for ECR image URI
4. Request database configuration
5. Optionally create Application Load Balancer and Target Group
6. Register task definition
7. Create or update ECS service
8. Wait for service to stabilize
9. Display deployment status and application URL

### Using the Deployment Script (Windows)

```cmd
cd scripts
deploy-image.bat
```

### Manual Deployment

#### 1. Update Task Definition

Replace placeholders in `ecs/task-definition.json`:
- `{{IMAGE_URI}}`: Your ECR image URI
- `{{AWS_REGION}}`: AWS region (e.g., us-east-1)
- `{{ACCOUNT_ID}}`: AWS account ID
- `{{DB_HOST}}`: Database host
- `{{DB_USERNAME}}`: Database username
- `{{DB_PASSWORD}}`: Database password

#### 2. Register Task Definition

```bash
aws ecs register-task-definition \
  --cli-input-json file://ecs/task-definition.json \
  --region us-east-1
```

#### 3. Create Service

```bash
aws ecs create-service \
  --cli-input-json file://ecs/service-definition.json \
  --region us-east-1
```

#### 4. Update Existing Service

```bash
aws ecs update-service \
  --cluster crm-cluster \
  --service crm-comp-service \
  --task-definition crm-comp-task \
  --force-new-deployment \
  --region us-east-1
```

---

## Monitoring and Logging

### CloudWatch Logs

View application logs in CloudWatch:

```bash
aws logs tail /ecs/crm-comp --follow --region us-east-1
```

Or access via AWS Console:
1. Navigate to CloudWatch → Log Groups
2. Select `/ecs/crm-comp`
3. View log streams for each task

### ECS Service Metrics

Monitor service health:

```bash
aws ecs describe-services \
  --cluster crm-cluster \
  --services crm-comp-service \
  --region us-east-1
```

### Application Health Check

Access the health endpoint:

```bash
curl http://<alb-dns-name>/appinfo/health
```

Expected response:
```json
{
  "status": "UP"
}
```

---

## Troubleshooting

### Common Issues

#### 1. Task Fails to Start

**Symptoms:** Tasks immediately transition to STOPPED state

**Possible Causes:**
- Invalid CPU/memory combination
- Image pull errors (ECR permissions)
- Container health check failures

**Solution:**
```bash
# Check task stopped reason
aws ecs describe-tasks \
  --cluster crm-cluster \
  --tasks <task-id> \
  --region us-east-1

# Check CloudWatch logs for errors
aws logs tail /ecs/crm-comp --since 5m
```

#### 2. Database Connection Failures

**Symptoms:** Application logs show database connection errors

**Solution:**
- Verify security group allows inbound traffic from ECS tasks
- Check database credentials in task definition
- Ensure database is accessible from VPC
- Test database connectivity:
  ```bash
  mysql -h <db-host> -u <username> -p<password>
  ```

#### 3. Load Balancer Health Check Failures

**Symptoms:** Tasks continuously restart, ALB marks targets unhealthy

**Solution:**
- Verify health check path: `/appinfo/health`
- Check health check timeout and interval settings
- Increase `healthCheckGracePeriodSeconds` to allow application startup
- Verify security group allows ALB to reach tasks on port 8080

#### 4. Out of Memory Errors

**Symptoms:** Tasks stop with exit code 137 or OOM errors

**Solution:**
- Increase task memory in task definition
- Adjust JVM heap size in `JAVA_OPTS`:
  ```json
  "JAVA_OPTS": "-Xmx768m -Xms384m"
  ```

#### 5. Slow Application Startup

**Symptoms:** Health checks fail during startup

**Solution:**
- Increase health check `startPeriod` in task definition:
  ```json
  "startPeriod": 120
  ```
- Monitor startup time in CloudWatch logs

### Debug Commands

```bash
# List running tasks
aws ecs list-tasks --cluster crm-cluster --region us-east-1

# Describe task details
aws ecs describe-tasks --cluster crm-cluster --tasks <task-id> --region us-east-1

# View service events
aws ecs describe-services \
  --cluster crm-cluster \
  --services crm-comp-service \
  --region us-east-1 \
  --query 'services[0].events'

# Check target health
aws elbv2 describe-target-health \
  --target-group-arn <target-group-arn> \
  --region us-east-1
```

---

## Security Best Practices

### 1. Use Secrets Manager for Sensitive Data

Store database credentials in AWS Secrets Manager:

```bash
aws secretsmanager create-secret \
  --name crm/db-credentials \
  --secret-string '{"username":"admin","password":"secret"}' \
  --region us-east-1
```

Reference in task definition:
```json
"secrets": [
  {
    "name": "DB_PASSWORD",
    "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789:secret:crm/db-credentials:password::"
  }
]
```

### 2. Use Private Subnets

Deploy tasks in private subnets with NAT Gateway for internet access.

### 3. Enable Container Insights

```bash
aws ecs update-cluster-settings \
  --cluster crm-cluster \
  --settings name=containerInsights,value=enabled \
  --region us-east-1
```

### 4. Implement Least Privilege IAM Policies

Grant only necessary permissions to task roles.

### 5. Enable VPC Flow Logs

Monitor network traffic:

```bash
aws ec2 create-flow-logs \
  --resource-type VPC \
  --resource-ids <vpc-id> \
  --traffic-type ALL \
  --log-destination-type cloud-watch-logs \
  --log-group-name /aws/vpc/flowlogs
```

---

## Scaling and Management

### Manual Scaling

Update desired task count:

```bash
aws ecs update-service \
  --cluster crm-cluster \
  --service crm-comp-service \
  --desired-count 4 \
  --region us-east-1
```

### Auto Scaling

Configure Service Auto Scaling:

```bash
# Register scalable target
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --resource-id service/crm-cluster/crm-comp-service \
  --scalable-dimension ecs:service:DesiredCount \
  --min-capacity 2 \
  --max-capacity 10 \
  --region us-east-1

# Create scaling policy (CPU-based)
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --resource-id service/crm-cluster/crm-comp-service \
  --scalable-dimension ecs:service:DesiredCount \
  --policy-name cpu-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration file://scaling-policy.json \
  --region us-east-1
```

`scaling-policy.json`:
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

Use AWS CodeDeploy for blue/green deployments:

1. Create CodeDeploy application and deployment group
2. Configure deployment settings
3. Deploy new task definition revision
4. Automatic traffic shifting and rollback

### Rolling Updates

ECS handles rolling updates automatically:
- New tasks are started with updated task definition
- Old tasks are stopped after new tasks are healthy
- Deployment circuit breaker automatically rolls back on failures

---

## Technology-Specific Notes

### Spring Boot Configuration

1. **Profiles:** Use `SPRING_PROFILES_ACTIVE` to switch between environments
2. **Actuator Endpoints:** Available at `/appinfo/*`
3. **Graceful Shutdown:** Spring Boot handles SIGTERM for graceful shutdown

### JVM Tuning for Containers

```bash
JAVA_OPTS="-Xmx768m -Xms384m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
```

- `-XX:+UseContainerSupport`: Enables container awareness
- `-XX:MaxRAMPercentage=75.0`: Limits heap to 75% of container memory

### Database Connection Pooling

For production, configure HikariCP in `application.properties`:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

---

## Support and Resources

- **AWS ECS Documentation:** https://docs.aws.amazon.com/ecs/
- **Spring Boot Documentation:** https://docs.spring.io/spring-boot/
- **Docker Documentation:** https://docs.docker.com/
- **AWS CLI Reference:** https://docs.aws.amazon.com/cli/

---

## Conclusion

This guide provides a comprehensive approach to deploying a Spring Boot application to AWS ECS Fargate. Follow the steps sequentially for a successful deployment. For production environments, ensure you implement all security best practices and monitoring solutions.

For questions or issues, consult the troubleshooting section or AWS support documentation.