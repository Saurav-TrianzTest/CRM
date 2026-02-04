@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Docker Build and Push Script
echo ========================================
echo.

REM Project configuration
set PROJECT_NAME=crm-app

REM Prompt for image tag
set /p IMAGE_TAG="Enter image tag (default: latest): "
if "!IMAGE_TAG!"=="" set IMAGE_TAG=latest

REM Sanitize tag using PowerShell
for /f "delims=" %%i in ('powershell -Command "'!IMAGE_TAG!' -replace '[^a-zA-Z0-9]', '-' -replace '^-+', '' -replace '-+$', '' | ForEach-Object { $_.ToLower() }"') do set IMAGE_TAG=%%i
if "!IMAGE_TAG!"=="" set IMAGE_TAG=latest

echo Using sanitized tag: !IMAGE_TAG!
echo.

REM Registry selection
echo Select container registry:
echo 1. AWS ECR (Elastic Container Registry)
echo 2. Docker Hub
set /p REGISTRY_CHOICE="Enter choice (1 or 2): "

if "!REGISTRY_CHOICE!"=="1" (
    echo.
    echo === AWS ECR Configuration ===
    set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
    set /p AWS_ACCOUNT_ID="Enter AWS Account ID: "
    set /p ECR_REPO="Enter ECR Repository Name (default: crm-app): "
    if "!ECR_REPO!"=="" set ECR_REPO=crm-app
    
    REM Sanitize repository name
    for /f "delims=" %%i in ('powershell -Command "'!ECR_REPO!' -replace '[^a-zA-Z0-9/_-]', '-' -replace '^-+', '' -replace '-+$', '' | ForEach-Object { $_.ToLower() }"') do set ECR_REPO=%%i
    
    set REGISTRY_URL=!AWS_ACCOUNT_ID!.dkr.ecr.!AWS_REGION!.amazonaws.com
    set FULL_IMAGE_NAME=!REGISTRY_URL!/!ECR_REPO!:!IMAGE_TAG!
    
    echo.
    echo Authenticating with AWS ECR...
    for /f "delims=" %%p in ('aws ecr get-login-password --region !AWS_REGION!') do set ECR_PASSWORD=%%p
    echo !ECR_PASSWORD! | docker login --username AWS --password-stdin !REGISTRY_URL!
    
    if !ERRORLEVEL! neq 0 (
        echo ERROR: ECR authentication failed
        exit /b 1
    )
    
    echo Checking if ECR repository exists...
    aws ecr describe-repositories --repository-names !ECR_REPO! --region !AWS_REGION! >nul 2>&1
    if !ERRORLEVEL! neq 0 (
        echo Repository does not exist. Creating ECR repository: !ECR_REPO!
        aws ecr create-repository --repository-name !ECR_REPO! --region !AWS_REGION!
        if !ERRORLEVEL! neq 0 (
            echo ERROR: Failed to create ECR repository
            exit /b 1
        )
    )
    
) else if "!REGISTRY_CHOICE!"=="2" (
    echo.
    echo === Docker Hub Configuration ===
    set /p DOCKER_USERNAME="Enter Docker Hub username: "
    set /p DOCKER_PASSWORD="Enter Docker Hub password or access token: "
    
    set FULL_IMAGE_NAME=!DOCKER_USERNAME!/!PROJECT_NAME!:!IMAGE_TAG!
    
    echo.
    echo Authenticating with Docker Hub...
    echo !DOCKER_PASSWORD! | docker login --username !DOCKER_USERNAME! --password-stdin
    
    if !ERRORLEVEL! neq 0 (
        echo ERROR: Docker Hub authentication failed
        exit /b 1
    )
) else (
    echo Invalid choice. Exiting.
    exit /b 1
)

echo.
echo ========================================
echo Building Docker image: !FULL_IMAGE_NAME!
echo ========================================

docker build -t "!FULL_IMAGE_NAME!" .

if !ERRORLEVEL! neq 0 (
    echo ERROR: Docker build failed
    exit /b 1
)

echo.
echo ========================================
echo Pushing image to registry
echo ========================================

docker push "!FULL_IMAGE_NAME!"

if !ERRORLEVEL! neq 0 (
    echo ERROR: Docker push failed
    exit /b 1
)

echo.
echo ========================================
echo SUCCESS!
echo ========================================
echo Image: !FULL_IMAGE_NAME!
echo Tag: !IMAGE_TAG!
echo.
echo Next steps:
echo 1. Run deploy-image.bat to deploy to AWS ECS
echo 2. Use this image URI in your ECS task definition
echo.

endlocal