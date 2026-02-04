@echo off
setlocal enabledelayedexpansion

echo ========================================
echo AWS ECS Fargate Deployment Script
echo ========================================
echo.

REM Prompt for configuration
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "
set /p VPC_ID="Enter VPC ID: "
set /p SUBNET_IDS="Enter Subnet IDs (comma-separated, at least 2): "
set /p SECURITY_GROUP="Enter Security Group ID: "
set /p IMAGE_URI="Enter ECR Image URI: "

echo.
set /p NEED_LB="Do you need a load balancer for this service? (y/n): "

REM Get AWS Account ID
echo.
echo Retrieving AWS Account ID...
for /f "delims=" %%i in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%i
if "!ACCOUNT_ID!"=="" (
    echo ERROR: Failed to retrieve AWS Account ID
    exit /b 1
)
echo Account ID: !ACCOUNT_ID!

REM Parse subnets
for /f "tokens=1,2 delims=," %%a in ("!SUBNET_IDS!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)

REM Check if cluster exists
echo.
echo Checking ECS cluster...
aws ecs describe-clusters --clusters "!CLUSTER_NAME!" --region "!AWS_REGION!" >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Cluster does not exist. Creating cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name "!CLUSTER_NAME!" --region "!AWS_REGION!"
    if !ERRORLEVEL! neq 0 (
        echo ERROR: Failed to create ECS cluster
        exit /b 1
    )
)
echo Cluster ready: !CLUSTER_NAME!

REM Handle load balancer
set TARGET_GROUP_ARN=
if /i "!NEED_LB!"=="y" (
    echo.
    echo Creating Application Load Balancer and Target Group...
    
    set LB_NAME=crm-app-alb
    set TG_NAME=crm-app-tg
    
    echo Creating ALB: !LB_NAME!
    for /f "delims=" %%i in ('aws elbv2 create-load-balancer --name "!LB_NAME!" --subnets !SUBNET_1! !SUBNET_2! --security-groups "!SECURITY_GROUP!" --scheme internet-facing --type application --region "!AWS_REGION!" --query "LoadBalancers[0].LoadBalancerArn" --output text 2^>nul') do set LB_ARN=%%i
    
    if "!LB_ARN!"=="" (
        for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --names "!LB_NAME!" --region "!AWS_REGION!" --query "LoadBalancers[0].LoadBalancerArn" --output text') do set LB_ARN=%%i
    )
    
    echo Creating Target Group: !TG_NAME!
    for /f "delims=" %%i in ('aws elbv2 create-target-group --name "!TG_NAME!" --protocol HTTP --port 8080 --vpc-id "!VPC_ID!" --target-type ip --health-check-path "/appinfo/health" --region "!AWS_REGION!" --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%i
    
    if "!TARGET_GROUP_ARN!"=="" (
        for /f "delims=" %%i in ('aws elbv2 describe-target-groups --names "!TG_NAME!" --region "!AWS_REGION!" --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%i
    )
    
    echo Creating ALB Listener...
    aws elbv2 create-listener --load-balancer-arn "!LB_ARN!" --protocol HTTP --port 80 --default-actions Type=forward,TargetGroupArn="!TARGET_GROUP_ARN!" --region "!AWS_REGION!" >nul 2>&1
    
    for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --load-balancer-arns "!LB_ARN!" --region "!AWS_REGION!" --query "LoadBalancers[0].DNSName" --output text') do set LB_DNS=%%i
    
    echo Load Balancer DNS: !LB_DNS!
    echo Target Group ARN: !TARGET_GROUP_ARN!
)

REM Update task definition
echo.
echo Preparing task definition...
copy /y ecs\task-definition.json ecs\task-definition-deployed.json >nul

powershell -Command "(Get-Content ecs\task-definition-deployed.json) -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content ecs\task-definition-deployed.json"
powershell -Command "(Get-Content ecs\task-definition-deployed.json) -replace '{{AWS_REGION}}', '!AWS_REGION!' | Set-Content ecs\task-definition-deployed.json"
powershell -Command "(Get-Content ecs\task-definition-deployed.json) -replace '{{ACCOUNT_ID}}', '!ACCOUNT_ID!' | Set-Content ecs\task-definition-deployed.json"

echo Registering task definition...
for /f "delims=" %%i in ('aws ecs register-task-definition --cli-input-json file://ecs/task-definition-deployed.json --region "!AWS_REGION!" --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%i

if "!TASK_DEF_ARN!"=="" (
    echo ERROR: Failed to register task definition
    exit /b 1
)

echo Task Definition ARN: !TASK_DEF_ARN!

REM Update service definition
echo.
echo Preparing service definition...
copy /y ecs\service-definition.json ecs\service-definition-deployed.json >nul

powershell -Command "(Get-Content ecs\service-definition-deployed.json) -replace '{{CLUSTER_NAME}}', '!CLUSTER_NAME!' | Set-Content ecs\service-definition-deployed.json"
powershell -Command "(Get-Content ecs\service-definition-deployed.json) -replace '{{SUBNET_1}}', '!SUBNET_1!' | Set-Content ecs\service-definition-deployed.json"
powershell -Command "(Get-Content ecs\service-definition-deployed.json) -replace '{{SUBNET_2}}', '!SUBNET_2!' | Set-Content ecs\service-definition-deployed.json"
powershell -Command "(Get-Content ecs\service-definition-deployed.json) -replace '{{SECURITY_GROUP}}', '!SECURITY_GROUP!' | Set-Content ecs\service-definition-deployed.json"

if /i "!NEED_LB!"=="y" (
    powershell -Command "(Get-Content ecs\service-definition-deployed.json) -replace '{{TARGET_GROUP_ARN}}', '!TARGET_GROUP_ARN!' | Set-Content ecs\service-definition-deployed.json"
)

set SERVICE_NAME=crm-app-service

REM Check if service exists
echo.
echo Checking if service exists...
for /f "delims=" %%i in ('aws ecs describe-services --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!" --query "services[?status==`ACTIVE`].serviceName" --output text') do set EXISTING_SERVICE=%%i

if "!EXISTING_SERVICE!"=="" (
    echo Service does not exist. Creating new service...
    aws ecs create-service --cli-input-json file://ecs/service-definition-deployed.json --region "!AWS_REGION!"
    if !ERRORLEVEL! neq 0 (
        echo ERROR: Failed to create service
        exit /b 1
    )
) else (
    echo Service exists. Updating service...
    aws ecs update-service --cluster "!CLUSTER_NAME!" --service "!SERVICE_NAME!" --task-definition "!TASK_DEF_ARN!" --region "!AWS_REGION!" --force-new-deployment
    if !ERRORLEVEL! neq 0 (
        echo ERROR: Failed to update service
        exit /b 1
    )
)

echo.
echo Waiting for service to stabilize...
aws ecs wait services-stable --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!"

echo.
echo ========================================
echo Deployment Status
echo ========================================

aws ecs describe-services --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!" --query "services[0].{Status:status,Running:runningCount,Desired:desiredCount}" --output table

echo.
echo ========================================
echo SUCCESS!
echo ========================================
echo Cluster: !CLUSTER_NAME!
echo Service: !SERVICE_NAME!
echo Task Definition: !TASK_DEF_ARN!
if /i "!NEED_LB!"=="y" (
    echo Load Balancer DNS: http://!LB_DNS!
)
echo CloudWatch Logs: /ecs/crm-app
echo.

endlocal