#!/bin/bash
set -e

echo "================================"
echo "Docker Build and Push Script"
echo "================================"
echo ""

# Project configuration
PROJECT_NAME="testjavacont3"

# Sanitize project name for Docker image
IMAGE_NAME=$(echo "$PROJECT_NAME" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9' '-' | sed 's/^-*//;s/-*$//')

echo "Project: $PROJECT_NAME"
echo "Image name: $IMAGE_NAME"
echo ""

# Prompt for image tag
read -p "Enter image tag (default: latest): " IMAGE_TAG
IMAGE_TAG=${IMAGE_TAG:-latest}

# Sanitize tag
IMAGE_TAG=$(echo "$IMAGE_TAG" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9.-' '-' | sed 's/^-*//;s/-*$//')
IMAGE_TAG=${IMAGE_TAG:-latest}

echo "Using tag: $IMAGE_TAG"
echo ""

# Select registry type
echo "Select container registry:"
echo "1. AWS ECR (Elastic Container Registry)"
echo "2. Docker Hub"
read -p "Enter choice (1 or 2): " REGISTRY_CHOICE

if [ "$REGISTRY_CHOICE" = "1" ]; then
    echo ""
    echo "=== AWS ECR Configuration ==="
    
    read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
    read -p "Enter ECR Repository Name (e.g., testjavacont3): " ECR_REPO
    
    # Get AWS Account ID
    echo "Retrieving AWS Account ID..."
    ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
    
    if [ -z "$ACCOUNT_ID" ]; then
        echo "Error: Failed to retrieve AWS Account ID. Please check AWS CLI configuration."
        exit 1
    fi
    
    echo "AWS Account ID: $ACCOUNT_ID"
    
    REGISTRY_URL="$ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"
    FULL_IMAGE_NAME="$REGISTRY_URL/$ECR_REPO:$IMAGE_TAG"
    
    echo ""
    echo "Authenticating with AWS ECR..."
    aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $REGISTRY_URL
    
    if [ $? -ne 0 ]; then
        echo "Error: ECR authentication failed"
        exit 1
    fi
    
    # Check if repository exists, create if not
    echo "Checking ECR repository..."
    aws ecr describe-repositories --repository-names $ECR_REPO --region $AWS_REGION >/dev/null 2>&1 || \
        aws ecr create-repository --repository-name $ECR_REPO --region $AWS_REGION
    
    echo "ECR authentication successful"
    
elif [ "$REGISTRY_CHOICE" = "2" ]; then
    echo ""
    echo "=== Docker Hub Configuration ==="
    
    read -p "Enter Docker Hub username: " DOCKER_USERNAME
    read -sp "Enter Docker Hub password/token: " DOCKER_PASSWORD
    echo ""
    
    FULL_IMAGE_NAME="$DOCKER_USERNAME/$IMAGE_NAME:$IMAGE_TAG"
    
    echo "Authenticating with Docker Hub..."
    echo "$DOCKER_PASSWORD" | docker login --username $DOCKER_USERNAME --password-stdin
    
    if [ $? -ne 0 ]; then
        echo "Error: Docker Hub authentication failed"
        exit 1
    fi
    
    echo "Docker Hub authentication successful"
else
    echo "Invalid choice. Exiting."
    exit 1
fi

echo ""
echo "=== Building Docker Image ==="
echo "Image: $FULL_IMAGE_NAME"
echo ""

docker build -t $FULL_IMAGE_NAME .

if [ $? -ne 0 ]; then
    echo "Error: Docker build failed"
    exit 1
fi

echo ""
echo "=== Pushing Docker Image ==="
echo ""

docker push $FULL_IMAGE_NAME

if [ $? -ne 0 ]; then
    echo "Error: Docker push failed"
    exit 1
fi

echo ""
echo "================================"
echo "Build and Push Completed Successfully!"
echo "================================"
echo "Image: $FULL_IMAGE_NAME"
echo ""