#!/bin/bash

# AWS ECS Fargate Deployment Script for CRM Application
# This script deploys the Docker image to AWS ECS Fargate

set -e
set -o pipefail

echo "=========================================="
echo "  CRM ECS Fargate Deployment Script  "
echo "=========================================="
echo ""

# Project configuration
PROJECT_NAME="crm-comp"
TASK_FAMILY="${PROJECT_NAME}-task"
SERVICE_NAME="${PROJECT_NAME}-service"

echo "Project: $PROJECT_NAME"
echo ""

# Prompt for AWS configuration
echo "=== AWS Configuration ==="
read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -p "Enter ECS Cluster Name: " CLUSTER_NAME

echo ""
echo "=== Network Configuration ==="
read -p "Enter VPC ID: " VPC_ID
read -p "Enter Subnet IDs (comma-separated, at least 2): " SUBNET_IDS
read -p "Enter Security Group ID: " SECURITY_GROUP

# Parse subnets
IFS=',' read -ra SUBNETS <<< "$SUBNET_IDS"
SUBNET_1="${SUBNETS[0]}"
SUBNET_2="${SUBNETS[1]:-${SUBNETS[0]}}"

echo ""
echo "=== Container Image Configuration ==="
read -p "Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/crm-comp:latest): " IMAGE_URI

echo ""
echo "=== Database Configuration ==="
read -p "Enter Database Host (e.g., mysql.example.com): " DB_HOST
read -p "Enter Database Username: " DB_USERNAME
read -sp "Enter Database Password: " DB_PASSWORD
echo ""

echo ""
echo "Getting AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
echo "Account ID: $ACCOUNT_ID"

echo ""
echo "Checking if ECS cluster exists..."
aws ecs describe-clusters --clusters "$CLUSTER_NAME" --region "$AWS_REGION" >/dev/null 2>&1 || {
    echo "Cluster does not exist. Creating ECS cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name "$CLUSTER_NAME" --region "$AWS_REGION"
    echo "Cluster created successfully."
}

echo ""
read -p "Do you need a load balancer for this service? (y/n): " NEED_LB

if [[ "$NEED_LB" =~ ^[Yy]$ ]]; then
    echo ""
    echo "Creating Application Load Balancer and Target Group..."
    
    # Create ALB
    ALB_NAME="${PROJECT_NAME}-alb"
    echo "Creating Application Load Balancer: $ALB_NAME"
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name "$ALB_NAME" \
        --subnets "$SUBNET_1" "$SUBNET_2" \
        --security-groups "$SECURITY_GROUP" \
        --scheme internet-facing \
        --type application \
        --ip-address-type ipv4 \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text 2>/dev/null || echo "")
    
    if [ -z "$ALB_ARN" ]; then
        echo "Load balancer may already exist, retrieving ARN..."
        ALB_ARN=$(aws elbv2 describe-load-balancers \
            --names "$ALB_NAME" \
            --region "$AWS_REGION" \
            --query 'LoadBalancers[0].LoadBalancerArn' \
            --output text)
    fi
    
    echo "Load Balancer ARN: $ALB_ARN"
    
    # Get ALB DNS name
    ALB_DNS=$(aws elbv2 describe-load-balancers \
        --load-balancer-arns "$ALB_ARN" \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].DNSName' \
        --output text)
    
    # Create Target Group
    TG_NAME="${PROJECT_NAME}-tg"
    echo "Creating Target Group: $TG_NAME"
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name "$TG_NAME" \
        --protocol HTTP \
        --port 8080 \
        --vpc-id "$VPC_ID" \
        --target-type ip \
        --health-check-enabled \
        --health-check-protocol HTTP \
        --health-check-path "/appinfo/health" \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region "$AWS_REGION" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || echo "")
    
    if [ -z "$TARGET_GROUP_ARN" ]; then
        echo "Target group may already exist, retrieving ARN..."
        TARGET_GROUP_ARN=$(aws elbv2 describe-target-groups \
            --names "$TG_NAME" \
            --region "$AWS_REGION" \
            --query 'TargetGroups[0].TargetGroupArn' \
            --output text)
    fi
    
    echo "Target Group ARN: $TARGET_GROUP_ARN"
    
    # Create Listener
    echo "Creating ALB Listener..."
    aws elbv2 create-listener \
        --load-balancer-arn "$ALB_ARN" \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn="$TARGET_GROUP_ARN" \
        --region "$AWS_REGION" >/dev/null 2>&1 || echo "Listener may already exist."
    
    echo "Load balancer setup completed."
    echo ""
else
    TARGET_GROUP_ARN=""
    echo "Skipping load balancer creation."
    echo ""
fi

echo "Creating temporary working directory..."
WORK_DIR=$(mktemp -d)
cd "$WORK_DIR"

echo "Copying ECS configuration files..."
cp "$(dirname "$0")/../ecs/task-definition.json" ./task-definition.json
cp "$(dirname "$0")/../ecs/service-definition.json" ./service-definition.json

echo ""
echo "Updating task definition with configuration..."
sed -i.bak "s|{{IMAGE_URI}}|$IMAGE_URI|g" task-definition.json
sed -i.bak "s|{{AWS_REGION}}|$AWS_REGION|g" task-definition.json
sed -i.bak "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" task-definition.json
sed -i.bak "s|{{DB_HOST}}|$DB_HOST|g" task-definition.json
sed -i.bak "s|{{DB_USERNAME}}|$DB_USERNAME|g" task-definition.json
sed -i.bak "s|{{DB_PASSWORD}}|$DB_PASSWORD|g" task-definition.json

echo "Updating service definition with configuration..."
sed -i.bak "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" service-definition.json
sed -i.bak "s|{{SUBNET_1}}|$SUBNET_1|g" service-definition.json
sed -i.bak "s|{{SUBNET_2}}|$SUBNET_2|g" service-definition.json
sed -i.bak "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" service-definition.json

if [ -n "$TARGET_GROUP_ARN" ]; then
    sed -i.bak "s|{{TARGET_GROUP_ARN}}|$TARGET_GROUP_ARN|g" service-definition.json
else
    # Remove loadBalancers section if no load balancer
    sed -i.bak '/"loadBalancers":/,/],/d' service-definition.json
    sed -i.bak '/"healthCheckGracePeriodSeconds":/d' service-definition.json
fi

echo ""
echo "Registering ECS task definition..."
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://task-definition.json \
    --region "$AWS_REGION" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "Task Definition ARN: $TASK_DEF_ARN"

echo ""
echo "Creating CloudWatch log group..."
aws logs create-log-group --log-group-name "/ecs/$PROJECT_NAME" --region "$AWS_REGION" 2>/dev/null || echo "Log group already exists."

echo ""
echo "Checking if service exists..."
EXISTING_SERVICE=$(aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[?status!=`INACTIVE`].serviceName' \
    --output text 2>/dev/null || echo "")

if [ -z "$EXISTING_SERVICE" ] || [ "$EXISTING_SERVICE" = "None" ]; then
    echo "Service does not exist. Creating new service..."
    aws ecs create-service \
        --cli-input-json file://service-definition.json \
        --region "$AWS_REGION"
    echo "Service created successfully."
else
    echo "Service exists. Updating service..."
    aws ecs update-service \
        --cluster "$CLUSTER_NAME" \
        --service "$SERVICE_NAME" \
        --task-definition "$TASK_DEF_ARN" \
        --force-new-deployment \
        --region "$AWS_REGION"
    echo "Service updated successfully."
fi

echo ""
echo "Waiting for service to become stable..."
aws ecs wait services-stable \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION"

echo ""
echo "Verifying deployment..."
aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[0].{ServiceName:serviceName,Status:status,RunningCount:runningCount,DesiredCount:desiredCount}' \
    --output table

echo ""
echo "=========================================="
echo "  Deployment Completed Successfully!  "
echo "=========================================="
echo ""
echo "Service: $SERVICE_NAME"
echo "Cluster: $CLUSTER_NAME"
echo "Task Definition: $TASK_DEF_ARN"
echo "CloudWatch Logs: /ecs/$PROJECT_NAME"

if [ -n "$ALB_DNS" ]; then
    echo ""
    echo "Application URL: http://$ALB_DNS"
    echo "Health Check: http://$ALB_DNS/appinfo/health"
fi

echo ""
echo "Cleaning up temporary files..."
cd -
rm -rf "$WORK_DIR"

echo ""
echo "Deployment complete!"
echo ""