#!/bin/bash
set -e
set -o pipefail

echo "========================================"
echo "AWS ECS Fargate Deployment Script"
echo "========================================"
echo ""

# Project configuration
PROJECT_NAME="testjavacont3"
TASK_FAMILY="testjavacont3-task"
SERVICE_NAME="testjavacont3-service"

echo "Project: $PROJECT_NAME"
echo ""

# Prompt for AWS configuration
read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -p "Enter ECS Cluster Name: " CLUSTER_NAME

echo ""
echo "=== Network Configuration ==="
read -p "Enter VPC ID: " VPC_ID
read -p "Enter Subnet IDs (comma-separated, e.g., subnet-xxx,subnet-yyy): " SUBNETS_INPUT
read -p "Enter Security Group ID: " SECURITY_GROUP

# Parse subnets
IFS=',' read -ra SUBNET_ARRAY <<< "$SUBNETS_INPUT"
SUBNET_1=$(echo "${SUBNET_ARRAY[0]}" | xargs)
SUBNET_2=$(echo "${SUBNET_ARRAY[1]:-$SUBNET_1}" | xargs)

echo ""
echo "=== Container Image ==="
read -p "Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/testjavacont3:latest): " IMAGE_URI

if [ -z "$IMAGE_URI" ]; then
    echo "Error: Image URI is required"
    exit 1
fi

echo ""
echo "=== Load Balancer Configuration ==="
read -p "Do you need a load balancer for this service? (y/n): " NEED_LB

if [ "$NEED_LB" = "y" ] || [ "$NEED_LB" = "Y" ]; then
    echo "Creating Application Load Balancer and Target Group..."
    
    # Create target group
    TARGET_GROUP_NAME="${PROJECT_NAME}-tg"
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name $TARGET_GROUP_NAME \
        --protocol HTTP \
        --port 8080 \
        --vpc-id $VPC_ID \
        --target-type ip \
        --health-check-protocol HTTP \
        --health-check-path /actuator/health \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region $AWS_REGION \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || aws elbv2 describe-target-groups \
        --names $TARGET_GROUP_NAME \
        --region $AWS_REGION \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text)
    
    echo "Target Group ARN: $TARGET_GROUP_ARN"
    USE_LB=true
else
    echo "Skipping load balancer configuration"
    USE_LB=false
fi

echo ""
echo "=== Retrieving AWS Account Information ==="
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

if [ -z "$ACCOUNT_ID" ]; then
    echo "Error: Failed to retrieve AWS Account ID"
    exit 1
fi

echo "AWS Account ID: $ACCOUNT_ID"
echo "AWS Region: $AWS_REGION"

echo ""
echo "=== Checking ECS Cluster ==="
aws ecs describe-clusters --clusters $CLUSTER_NAME --region $AWS_REGION >/dev/null 2>&1 || {
    echo "Creating ECS cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name $CLUSTER_NAME --region $AWS_REGION
}

echo "ECS Cluster: $CLUSTER_NAME (ready)"

echo ""
echo "=== Preparing Task Definition ==="

# Create temporary task definition with replacements
TASK_DEF_FILE="ecs/task-definition.json"
TEMP_TASK_DEF="/tmp/task-definition-${PROJECT_NAME}.json"

cp $TASK_DEF_FILE $TEMP_TASK_DEF

# Replace placeholders
sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" $TEMP_TASK_DEF
sed -i "s|{{AWS_REGION}}|$AWS_REGION|g" $TEMP_TASK_DEF
sed -i "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" $TEMP_TASK_DEF

echo "Task definition prepared: $TEMP_TASK_DEF"

echo ""
echo "=== Registering Task Definition ==="
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://$TEMP_TASK_DEF \
    --region $AWS_REGION \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

if [ -z "$TASK_DEF_ARN" ]; then
    echo "Error: Failed to register task definition"
    exit 1
fi

echo "Task Definition ARN: $TASK_DEF_ARN"

echo ""
echo "=== Preparing Service Definition ==="

SERVICE_DEF_FILE="ecs/service-definition.json"
TEMP_SERVICE_DEF="/tmp/service-definition-${PROJECT_NAME}.json"

cp $SERVICE_DEF_FILE $TEMP_SERVICE_DEF

# Replace placeholders
sed -i "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" $TEMP_SERVICE_DEF
sed -i "s|{{SUBNET_1}}|$SUBNET_1|g" $TEMP_SERVICE_DEF
sed -i "s|{{SUBNET_2}}|$SUBNET_2|g" $TEMP_SERVICE_DEF
sed -i "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" $TEMP_SERVICE_DEF

# Handle load balancer configuration
if [ "$USE_LB" = true ]; then
    sed -i "s|{{TARGET_GROUP_ARN}}|$TARGET_GROUP_ARN|g" $TEMP_SERVICE_DEF
else
    # Remove loadBalancers section from service definition
    python3 -c "
import json
import sys

with open('$TEMP_SERVICE_DEF', 'r') as f:
    service_def = json.load(f)

if 'loadBalancers' in service_def:
    del service_def['loadBalancers']
if 'healthCheckGracePeriodSeconds' in service_def:
    del service_def['healthCheckGracePeriodSeconds']

with open('$TEMP_SERVICE_DEF', 'w') as f:
    json.dump(service_def, f, indent=2)
" 2>/dev/null || {
        # Fallback if python3 is not available
        sed -i '/"loadBalancers":/,/],/d' $TEMP_SERVICE_DEF
        sed -i '/"healthCheckGracePeriodSeconds":/d' $TEMP_SERVICE_DEF
    }
fi

echo "Service definition prepared: $TEMP_SERVICE_DEF"

echo ""
echo "=== Checking Service Existence ==="

SERVICE_EXISTS=$(aws ecs describe-services \
    --cluster $CLUSTER_NAME \
    --services $SERVICE_NAME \
    --region $AWS_REGION \
    --query 'services[?status==`ACTIVE`].serviceName' \
    --output text 2>/dev/null || echo "")

if [ -z "$SERVICE_EXISTS" ]; then
    echo "Creating new ECS service: $SERVICE_NAME"
    
    aws ecs create-service \
        --cluster $CLUSTER_NAME \
        --service-name $SERVICE_NAME \
        --cli-input-json file://$TEMP_SERVICE_DEF \
        --region $AWS_REGION
    
    echo "Service created successfully"
else
    echo "Updating existing ECS service: $SERVICE_NAME"
    
    aws ecs update-service \
        --cluster $CLUSTER_NAME \
        --service $SERVICE_NAME \
        --task-definition $TASK_DEF_ARN \
        --force-new-deployment \
        --region $AWS_REGION
    
    echo "Service updated successfully"
fi

echo ""
echo "=== Waiting for Service Stability ==="
echo "This may take several minutes..."

aws ecs wait services-stable \
    --cluster $CLUSTER_NAME \
    --services $SERVICE_NAME \
    --region $AWS_REGION

echo ""
echo "=== Deployment Verification ==="

aws ecs describe-services \
    --cluster $CLUSTER_NAME \
    --services $SERVICE_NAME \
    --region $AWS_REGION \
    --query 'services[0].{ServiceName:serviceName,Status:status,DesiredCount:desiredCount,RunningCount:runningCount,TaskDefinition:taskDefinition}' \
    --output table

echo ""
echo "========================================"
echo "Deployment Completed Successfully!"
echo "========================================"
echo "Cluster: $CLUSTER_NAME"
echo "Service: $SERVICE_NAME"
echo "Task Definition: $TASK_DEF_ARN"

if [ "$USE_LB" = true ]; then
    echo "Target Group: $TARGET_GROUP_ARN"
    echo ""
    echo "Note: Configure your Application Load Balancer listener to forward traffic to this target group"
fi

echo ""
echo "CloudWatch Logs: /ecs/$PROJECT_NAME"
echo "Region: $AWS_REGION"
echo ""
echo "View logs with:"
echo "aws logs tail /ecs/$PROJECT_NAME --follow --region $AWS_REGION"
echo ""

# Cleanup temporary files
rm -f $TEMP_TASK_DEF $TEMP_SERVICE_DEF