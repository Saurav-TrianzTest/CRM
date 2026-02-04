#!/bin/bash

set -e
set -o pipefail

echo "========================================"
echo "AWS ECS Fargate Deployment Script"
echo "========================================"
echo ""

# Prompt for configuration
read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -p "Enter ECS Cluster Name: " CLUSTER_NAME
read -p "Enter VPC ID: " VPC_ID
read -p "Enter Subnet IDs (comma-separated, at least 2): " SUBNET_IDS
read -p "Enter Security Group ID: " SECURITY_GROUP
read -p "Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/crm-app:latest): " IMAGE_URI

echo ""
read -p "Do you need a load balancer for this service? (y/n): " NEED_LB

# Get AWS Account ID
echo ""
echo "Retrieving AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
if [ -z "$ACCOUNT_ID" ]; then
    echo "ERROR: Failed to retrieve AWS Account ID"
    exit 1
fi
echo "Account ID: $ACCOUNT_ID"

# Convert comma-separated subnets to array
IFS=',' read -ra SUBNET_ARRAY <<< "$SUBNET_IDS"
SUBNET_1="${SUBNET_ARRAY[0]}"
SUBNET_2="${SUBNET_ARRAY[1]}"

# Check if cluster exists, create if not
echo ""
echo "Checking ECS cluster..."
aws ecs describe-clusters --clusters "$CLUSTER_NAME" --region "$AWS_REGION" >/dev/null 2>&1 || {
    echo "Cluster does not exist. Creating cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name "$CLUSTER_NAME" --region "$AWS_REGION"
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to create ECS cluster"
        exit 1
    fi
}
echo "Cluster ready: $CLUSTER_NAME"

# Handle load balancer
TARGET_GROUP_ARN=""
if [[ "$NEED_LB" =~ ^[Yy]$ ]]; then
    echo ""
    echo "Creating Application Load Balancer and Target Group..."
    
    LB_NAME="crm-app-alb"
    TG_NAME="crm-app-tg"
    
    # Create load balancer
    echo "Creating ALB: $LB_NAME"
    LB_ARN=$(aws elbv2 create-load-balancer \
        --name "$LB_NAME" \
        --subnets $SUBNET_1 $SUBNET_2 \
        --security-groups "$SECURITY_GROUP" \
        --scheme internet-facing \
        --type application \
        --ip-address-type ipv4 \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text 2>/dev/null || aws elbv2 describe-load-balancers --names "$LB_NAME" --region "$AWS_REGION" --query 'LoadBalancers[0].LoadBalancerArn' --output text)
    
    if [ -z "$LB_ARN" ]; then
        echo "ERROR: Failed to create or retrieve load balancer"
        exit 1
    fi
    
    # Create target group with target-type ip (required for Fargate)
    echo "Creating Target Group: $TG_NAME"
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name "$TG_NAME" \
        --protocol HTTP \
        --port 8080 \
        --vpc-id "$VPC_ID" \
        --target-type ip \
        --health-check-enabled \
        --health-check-path "/appinfo/health" \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --matcher HttpCode=200 \
        --region "$AWS_REGION" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || aws elbv2 describe-target-groups --names "$TG_NAME" --region "$AWS_REGION" --query 'TargetGroups[0].TargetGroupArn' --output text)
    
    if [ -z "$TARGET_GROUP_ARN" ]; then
        echo "ERROR: Failed to create or retrieve target group"
        exit 1
    fi
    
    # Create listener
    echo "Creating ALB Listener..."
    aws elbv2 create-listener \
        --load-balancer-arn "$LB_ARN" \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn="$TARGET_GROUP_ARN" \
        --region "$AWS_REGION" >/dev/null 2>&1 || echo "Listener may already exist"
    
    # Get ALB DNS name
    LB_DNS=$(aws elbv2 describe-load-balancers --load-balancer-arns "$LB_ARN" --region "$AWS_REGION" --query 'LoadBalancers[0].DNSName' --output text)
    
    echo "Load Balancer DNS: $LB_DNS"
    echo "Target Group ARN: $TARGET_GROUP_ARN"
fi

# Update task definition JSON
echo ""
echo "Preparing task definition..."
cp ecs/task-definition.json ecs/task-definition-deployed.json

sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" ecs/task-definition-deployed.json
sed -i "s|{{AWS_REGION}}|$AWS_REGION|g" ecs/task-definition-deployed.json
sed -i "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" ecs/task-definition-deployed.json

echo "Registering task definition..."
REGISTER_OUTPUT=$(aws ecs register-task-definition \
    --cli-input-json file://ecs/task-definition-deployed.json \
    --region "$AWS_REGION")

TASK_DEF_ARN=$(echo "$REGISTER_OUTPUT" | jq -r '.taskDefinition.taskDefinitionArn')

if [ -z "$TASK_DEF_ARN" ] || [ "$TASK_DEF_ARN" = "null" ]; then
    echo "ERROR: Failed to register task definition"
    exit 1
fi

echo "Task Definition ARN: $TASK_DEF_ARN"

# Update service definition JSON
echo ""
echo "Preparing service definition..."
cp ecs/service-definition.json ecs/service-definition-deployed.json

sed -i "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" ecs/service-definition-deployed.json
sed -i "s|{{SUBNET_1}}|$SUBNET_1|g" ecs/service-definition-deployed.json
sed -i "s|{{SUBNET_2}}|$SUBNET_2|g" ecs/service-definition-deployed.json
sed -i "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" ecs/service-definition-deployed.json

if [[ "$NEED_LB" =~ ^[Yy]$ ]]; then
    sed -i "s|{{TARGET_GROUP_ARN}}|$TARGET_GROUP_ARN|g" ecs/service-definition-deployed.json
else
    # Remove loadBalancers section if no LB needed
    jq 'del(.loadBalancers)' ecs/service-definition-deployed.json > ecs/service-definition-deployed-tmp.json
    mv ecs/service-definition-deployed-tmp.json ecs/service-definition-deployed.json
fi

SERVICE_NAME="crm-app-service"

# Check if service exists
echo ""
echo "Checking if service exists..."
EXISTING_SERVICE=$(aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[?status==`ACTIVE`].serviceName' \
    --output text)

if [ -z "$EXISTING_SERVICE" ] || [ "$EXISTING_SERVICE" = "None" ]; then
    echo "Service does not exist. Creating new service..."
    aws ecs create-service \
        --cli-input-json file://ecs/service-definition-deployed.json \
        --region "$AWS_REGION"
    
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to create service"
        exit 1
    fi
else
    echo "Service exists. Updating service..."
    aws ecs update-service \
        --cluster "$CLUSTER_NAME" \
        --service "$SERVICE_NAME" \
        --task-definition "$TASK_DEF_ARN" \
        --region "$AWS_REGION" \
        --force-new-deployment
    
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to update service"
        exit 1
    fi
fi

echo ""
echo "Waiting for service to stabilize..."
aws ecs wait services-stable \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION"

if [ $? -ne 0 ]; then
    echo "WARNING: Service did not stabilize within expected time"
fi

# Verify deployment
echo ""
echo "========================================"
echo "Deployment Status"
echo "========================================"

aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[0].{Status:status,Running:runningCount,Desired:desiredCount}' \
    --output table

echo ""
echo "========================================"
echo "SUCCESS!"
echo "========================================"
echo "Cluster: $CLUSTER_NAME"
echo "Service: $SERVICE_NAME"
echo "Task Definition: $TASK_DEF_ARN"
if [[ "$NEED_LB" =~ ^[Yy]$ ]]; then
    echo "Load Balancer DNS: http://$LB_DNS"
fi
echo "CloudWatch Logs: /ecs/crm-app"
echo ""
echo "Monitor your deployment:"
echo "aws ecs describe-services --cluster $CLUSTER_NAME --services $SERVICE_NAME --region $AWS_REGION"
echo ""