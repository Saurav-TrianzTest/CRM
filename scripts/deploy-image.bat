@echo off
setlocal enabledelayedexpansion

echo ==========================================
echo   CRM ECS Fargate Deployment Script
echo ==========================================
echo.

REM Project configuration
set PROJECT_NAME=crm-comp
set TASK_FAMILY=%PROJECT_NAME%-task
set SERVICE_NAME=%PROJECT_NAME%-service

echo Project: %PROJECT_NAME%
echo.

REM Prompt for AWS configuration
echo === AWS Configuration ===
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "

echo.
echo === Network Configuration ===
set /p VPC_ID="Enter VPC ID: "
set /p SUBNET_IDS="Enter Subnet IDs (comma-separated, at least 2): "
set /p SECURITY_GROUP="Enter Security Group ID: "

REM Parse subnets
for /f "tokens=1,2 delims=," %%a in ("!SUBNET_IDS!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)
if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!

echo.
echo === Container Image Configuration ===
set /p IMAGE_URI="Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/crm-comp:latest): "

echo.
echo === Database Configuration ===
set /p DB_HOST="Enter Database Host (e.g., mysql.example.com): "
set /p DB_USERNAME="Enter Database Username: "
set /p DB_PASSWORD="Enter Database Password: "

echo.
echo Getting AWS Account ID...
for /f "delims=" %%i in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%i
echo Account ID: !ACCOUNT_ID!

echo.
echo Checking if ECS cluster exists...
aws ecs describe-clusters --clusters "!CLUSTER_NAME!" --region "!AWS_REGION!" >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Cluster does not exist. Creating ECS cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name "!CLUSTER_NAME!" --region "!AWS_REGION!"
    echo Cluster created successfully.
)

echo.
set /p NEED_LB="Do you need a load balancer for this service? (y/n): "

if /i "!NEED_LB!"=="y" (
    echo.
    echo Creating Application Load Balancer and Target Group...
    
    set ALB_NAME=!PROJECT_NAME!-alb
    echo Creating Application Load Balancer: !ALB_NAME!
    
    for /f "delims=" %%i in ('aws elbv2 create-load-balancer --name "!ALB_NAME!" --subnets "!SUBNET_1!" "!SUBNET_2!" --security-groups "!SECURITY_GROUP!" --scheme internet-facing --type application --ip-address-type ipv4 --region "!AWS_REGION!" --query "LoadBalancers[0].LoadBalancerArn" --output text 2^>nul') do set ALB_ARN=%%i
    
    if "!ALB_ARN!"=="" (
        echo Load balancer may already exist, retrieving ARN...
        for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --names "!ALB_NAME!" --region "!AWS_REGION!" --query "LoadBalancers[0].LoadBalancerArn" --output text') do set ALB_ARN=%%i
    )
    
    echo Load Balancer ARN: !ALB_ARN!
    
    for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --load-balancer-arns "!ALB_ARN!" --region "!AWS_REGION!" --query "LoadBalancers[0].DNSName" --output text') do set ALB_DNS=%%i
    
    set TG_NAME=!PROJECT_NAME!-tg
    echo Creating Target Group: !TG_NAME!
    
    for /f "delims=" %%i in ('aws elbv2 create-target-group --name "!TG_NAME!" --protocol HTTP --port 8080 --vpc-id "!VPC_ID!" --target-type ip --health-check-enabled --health-check-protocol HTTP --health-check-path "/appinfo/health" --health-check-interval-seconds 30 --health-check-timeout-seconds 5 --healthy-threshold-count 2 --unhealthy-threshold-count 3 --region "!AWS_REGION!" --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%i
    
    if "!TARGET_GROUP_ARN!"=="" (
        echo Target group may already exist, retrieving ARN...
        for /f "delims=" %%i in ('aws elbv2 describe-target-groups --names "!TG_NAME!" --region "!AWS_REGION!" --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%i
    )
    
    echo Target Group ARN: !TARGET_GROUP_ARN!
    
    echo Creating ALB Listener...
    aws elbv2 create-listener --load-balancer-arn "!ALB_ARN!" --protocol HTTP --port 80 --default-actions Type=forward,TargetGroupArn="!TARGET_GROUP_ARN!" --region "!AWS_REGION!" >nul 2>&1
    
    echo Load balancer setup completed.
    echo.
) else (
    set TARGET_GROUP_ARN=
    echo Skipping load balancer creation.
    echo.
)

echo Creating temporary working directory...
set WORK_DIR=%TEMP%\ecs-deploy-%RANDOM%
mkdir "!WORK_DIR!"

echo Copying ECS configuration files...
copy "%~dp0..\ecs\task-definition.json" "!WORK_DIR!\task-definition.json" >nul
copy "%~dp0..\ecs\service-definition.json" "!WORK_DIR!\service-definition.json" >nul

echo.
echo Updating task definition with configuration...
powershell -command "(Get-Content '!WORK_DIR!\task-definition.json') -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content '!WORK_DIR!\task-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\task-definition.json') -replace '{{AWS_REGION}}', '!AWS_REGION!' | Set-Content '!WORK_DIR!\task-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\task-definition.json') -replace '{{ACCOUNT_ID}}', '!ACCOUNT_ID!' | Set-Content '!WORK_DIR!\task-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\task-definition.json') -replace '{{DB_HOST}}', '!DB_HOST!' | Set-Content '!WORK_DIR!\task-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\task-definition.json') -replace '{{DB_USERNAME}}', '!DB_USERNAME!' | Set-Content '!WORK_DIR!\task-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\task-definition.json') -replace '{{DB_PASSWORD}}', '!DB_PASSWORD!' | Set-Content '!WORK_DIR!\task-definition.json'"

echo Updating service definition with configuration...
powershell -command "(Get-Content '!WORK_DIR!\service-definition.json') -replace '{{CLUSTER_NAME}}', '!CLUSTER_NAME!' | Set-Content '!WORK_DIR!\service-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\service-definition.json') -replace '{{SUBNET_1}}', '!SUBNET_1!' | Set-Content '!WORK_DIR!\service-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\service-definition.json') -replace '{{SUBNET_2}}', '!SUBNET_2!' | Set-Content '!WORK_DIR!\service-definition.json'"
powershell -command "(Get-Content '!WORK_DIR!\service-definition.json') -replace '{{SECURITY_GROUP}}', '!SECURITY_GROUP!' | Set-Content '!WORK_DIR!\service-definition.json'"

if not "!TARGET_GROUP_ARN!"=="" (
    powershell -command "(Get-Content '!WORK_DIR!\service-definition.json') -replace '{{TARGET_GROUP_ARN}}', '!TARGET_GROUP_ARN!' | Set-Content '!WORK_DIR!\service-definition.json'"
) else (
    powershell -command "(Get-Content '!WORK_DIR!\service-definition.json') -replace '\s*\"loadBalancers\":[\s\S]*?\],', '' | Set-Content '!WORK_DIR!\service-definition.json'"
    powershell -command "(Get-Content '!WORK_DIR!\service-definition.json') -replace '\s*\"healthCheckGracePeriodSeconds\":\s*\d+,', '' | Set-Content '!WORK_DIR!\service-definition.json'"
)

echo.
echo Registering ECS task definition...
for /f "delims=" %%i in ('aws ecs register-task-definition --cli-input-json file://"!WORK_DIR!\task-definition.json" --region "!AWS_REGION!" --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%i

echo Task Definition ARN: !TASK_DEF_ARN!

echo.
echo Creating CloudWatch log group...
aws logs create-log-group --log-group-name "/ecs/!PROJECT_NAME!" --region "!AWS_REGION!" 2>nul

echo.
echo Checking if service exists...
for /f "delims=" %%i in ('aws ecs describe-services --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!" --query "services[?status!=`INACTIVE`].serviceName" --output text 2^>nul') do set EXISTING_SERVICE=%%i

if "!EXISTING_SERVICE!"=="" (
    echo Service does not exist. Creating new service...
    aws ecs create-service --cli-input-json file://"!WORK_DIR!\service-definition.json" --region "!AWS_REGION!"
    echo Service created successfully.
) else (
    echo Service exists. Updating service...
    aws ecs update-service --cluster "!CLUSTER_NAME!" --service "!SERVICE_NAME!" --task-definition "!TASK_DEF_ARN!" --force-new-deployment --region "!AWS_REGION!"
    echo Service updated successfully.
)

echo.
echo Waiting for service to become stable...
aws ecs wait services-stable --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!"

echo.
echo Verifying deployment...
aws ecs describe-services --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!" --query "services[0].{ServiceName:serviceName,Status:status,RunningCount:runningCount,DesiredCount:desiredCount}" --output table

echo.
echo ==========================================
echo   Deployment Completed Successfully!
echo ==========================================
echo.
echo Service: !SERVICE_NAME!
echo Cluster: !CLUSTER_NAME!
echo Task Definition: !TASK_DEF_ARN!
echo CloudWatch Logs: /ecs/!PROJECT_NAME!

if not "!ALB_DNS!"=="" (
    echo.
    echo Application URL: http://!ALB_DNS!
    echo Health Check: http://!ALB_DNS!/appinfo/health
)

echo.
echo Cleaning up temporary files...
rmdir /s /q "!WORK_DIR!"

echo.
echo Deployment complete!
echo.

endlocal