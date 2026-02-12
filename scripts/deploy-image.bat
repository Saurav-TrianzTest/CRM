@echo off
setlocal enabledelayedexpansion

echo ========================================
echo AWS ECS Fargate Deployment Script
echo ========================================
echo.

REM Project configuration
set PROJECT_NAME=testjavacont3
set TASK_FAMILY=testjavacont3-task
set SERVICE_NAME=testjavacont3-service

echo Project: %PROJECT_NAME%
echo.

REM Prompt for AWS configuration
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "

echo.
echo === Network Configuration ===
set /p VPC_ID="Enter VPC ID: "
set /p SUBNETS_INPUT="Enter Subnet IDs (comma-separated): "
set /p SECURITY_GROUP="Enter Security Group ID: "

REM Parse subnets
for /f "tokens=1,2 delims=," %%a in ("!SUBNETS_INPUT!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)
if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!

echo.
echo === Container Image ===
set /p IMAGE_URI="Enter ECR Image URI: "

if "!IMAGE_URI!"=="" (
    echo Error: Image URI is required
    exit /b 1
)

echo.
echo === Load Balancer Configuration ===
set /p NEED_LB="Do you need a load balancer for this service? (y/n): "

if /i "!NEED_LB!"=="y" (
    echo Creating Target Group...
    
    set TARGET_GROUP_NAME=!PROJECT_NAME!-tg
    
    for /f "tokens=*" %%a in ('aws elbv2 create-target-group --name !TARGET_GROUP_NAME! --protocol HTTP --port 8080 --vpc-id !VPC_ID! --target-type ip --health-check-protocol HTTP --health-check-path /actuator/health --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%a
    
    if "!TARGET_GROUP_ARN!"=="" (
        for /f "tokens=*" %%a in ('aws elbv2 describe-target-groups --names !TARGET_GROUP_NAME! --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%a
    )
    
    echo Target Group ARN: !TARGET_GROUP_ARN!
    set USE_LB=true
) else (
    echo Skipping load balancer configuration
    set USE_LB=false
)

echo.
echo === Retrieving AWS Account Information ===

for /f "tokens=*" %%a in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%a

if "!ACCOUNT_ID!"=="" (
    echo Error: Failed to retrieve AWS Account ID
    exit /b 1
)

echo AWS Account ID: !ACCOUNT_ID!
echo AWS Region: !AWS_REGION!

echo.
echo === Checking ECS Cluster ===

aws ecs describe-clusters --clusters !CLUSTER_NAME! --region !AWS_REGION! >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Creating ECS cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name !CLUSTER_NAME! --region !AWS_REGION!
)

echo ECS Cluster: !CLUSTER_NAME! (ready)

echo.
echo === Preparing Task Definition ===

set TASK_DEF_FILE=ecs\task-definition.json
set TEMP_TASK_DEF=%TEMP%\task-definition-!PROJECT_NAME!.json

copy !TASK_DEF_FILE! !TEMP_TASK_DEF! >nul

REM Replace placeholders using PowerShell
powershell -Command "(Get-Content '!TEMP_TASK_DEF!') -replace '{{IMAGE_URI}}', '!IMAGE_URI!' -replace '{{AWS_REGION}}', '!AWS_REGION!' -replace '{{ACCOUNT_ID}}', '!ACCOUNT_ID!' | Set-Content '!TEMP_TASK_DEF!'"

echo Task definition prepared

echo.
echo === Registering Task Definition ===

for /f "tokens=*" %%a in ('aws ecs register-task-definition --cli-input-json file://!TEMP_TASK_DEF! --region !AWS_REGION! --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%a

if "!TASK_DEF_ARN!"=="" (
    echo Error: Failed to register task definition
    exit /b 1
)

echo Task Definition ARN: !TASK_DEF_ARN!

echo.
echo === Preparing Service Definition ===

set SERVICE_DEF_FILE=ecs\service-definition.json
set TEMP_SERVICE_DEF=%TEMP%\service-definition-!PROJECT_NAME!.json

copy !SERVICE_DEF_FILE! !TEMP_SERVICE_DEF! >nul

REM Replace placeholders
powershell -Command "(Get-Content '!TEMP_SERVICE_DEF!') -replace '{{CLUSTER_NAME}}', '!CLUSTER_NAME!' -replace '{{SUBNET_1}}', '!SUBNET_1!' -replace '{{SUBNET_2}}', '!SUBNET_2!' -replace '{{SECURITY_GROUP}}', '!SECURITY_GROUP!' | Set-Content '!TEMP_SERVICE_DEF!'"

if "!USE_LB!"=="true" (
    powershell -Command "(Get-Content '!TEMP_SERVICE_DEF!') -replace '{{TARGET_GROUP_ARN}}', '!TARGET_GROUP_ARN!' | Set-Content '!TEMP_SERVICE_DEF!'"
) else (
    powershell -Command "$json = Get-Content '!TEMP_SERVICE_DEF!' | ConvertFrom-Json; $json.PSObject.Properties.Remove('loadBalancers'); $json.PSObject.Properties.Remove('healthCheckGracePeriodSeconds'); $json | ConvertTo-Json -Depth 10 | Set-Content '!TEMP_SERVICE_DEF!'"
)

echo Service definition prepared

echo.
echo === Checking Service Existence ===

for /f "tokens=*" %%a in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION! --query "services[?status==`ACTIVE`].serviceName" --output text 2^>nul') do set SERVICE_EXISTS=%%a

if "!SERVICE_EXISTS!"=="" (
    echo Creating new ECS service: !SERVICE_NAME!
    
    aws ecs create-service --cluster !CLUSTER_NAME! --service-name !SERVICE_NAME! --cli-input-json file://!TEMP_SERVICE_DEF! --region !AWS_REGION!
    
    if !ERRORLEVEL! neq 0 (
        echo Error: Failed to create service
        exit /b 1
    )
    
    echo Service created successfully
) else (
    echo Updating existing ECS service: !SERVICE_NAME!
    
    aws ecs update-service --cluster !CLUSTER_NAME! --service !SERVICE_NAME! --task-definition !TASK_DEF_ARN! --force-new-deployment --region !AWS_REGION!
    
    if !ERRORLEVEL! neq 0 (
        echo Error: Failed to update service
        exit /b 1
    )
    
    echo Service updated successfully
)

echo.
echo === Waiting for Service Stability ===
echo This may take several minutes...

aws ecs wait services-stable --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION!

echo.
echo === Deployment Verification ===

aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION! --query "services[0].{ServiceName:serviceName,Status:status,DesiredCount:desiredCount,RunningCount:runningCount}" --output table

echo.
echo ========================================
echo Deployment Completed Successfully!
echo ========================================
echo Cluster: !CLUSTER_NAME!
echo Service: !SERVICE_NAME!
echo Task Definition: !TASK_DEF_ARN!

if "!USE_LB!"=="true" (
    echo Target Group: !TARGET_GROUP_ARN!
    echo.
    echo Note: Configure your ALB listener to forward traffic to this target group
)

echo.
echo CloudWatch Logs: /ecs/!PROJECT_NAME!
echo Region: !AWS_REGION!
echo.

REM Cleanup
del !TEMP_TASK_DEF! !TEMP_SERVICE_DEF! 2>nul

endlocal