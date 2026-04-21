# Azure Cloud Readiness Configuration Guide

## Overview
This application has been migrated to be cloud-ready for Azure deployment. All local file system dependencies and non-cloud-native patterns have been replaced with Azure-native services.

## Changes Summary

### 1. File Storage Migration (Azure Blob Storage)
**Files Modified:**
- `src/main/java/crm/utils/ReadDataUtils.java`
- `src/main/java/crm/controller/PdfController.java`
- `src/main/java/crm/csv/CSVTest.java`

**Changes:**
- Replaced local file system operations with Azure Blob Storage
- All file reads/writes now use Azure Storage SDK
- Files are stored in Azure Blob Storage containers instead of local disk

### 2. Time/Scheduling Migration (Azure Service Bus)
**Files Modified:**
- `src/main/java/crm/controller/DateTimeTestController.java`

**Changes:**
- Replaced `java.util.Date` with `java.time.Instant` for timezone-agnostic timestamps
- Removed dependency on local server timers
- Added support for configurable timezone (defaults to UTC)
- Documented migration path to Azure Service Bus Scheduled Messages for distributed scheduling

### 3. Configuration Updates
**Files Modified:**
- `pom.xml` - Added Azure SDK dependencies
- `src/main/resources/application.properties` - Added Azure configuration properties

## Required Azure Resources

### 1. Azure Storage Account
Create an Azure Storage Account for file storage:

```bash
# Create resource group
az group create --name crm-app-rg --location eastus

# Create storage account
az storage account create \
  --name crmappstorage \
  --resource-group crm-app-rg \
  --location eastus \
  --sku Standard_LRS

# Get connection string
az storage account show-connection-string \
  --name crmappstorage \
  --resource-group crm-app-rg \
  --output tsv
```

### 2. Azure Service Bus (Optional - for scheduled tasks)
Create an Azure Service Bus namespace for distributed scheduling:

```bash
# Create Service Bus namespace
az servicebus namespace create \
  --name crm-app-servicebus \
  --resource-group crm-app-rg \
  --location eastus \
  --sku Standard

# Create queue
az servicebus queue create \
  --name scheduled-tasks \
  --namespace-name crm-app-servicebus \
  --resource-group crm-app-rg

# Get connection string
az servicebus namespace authorization-rule keys list \
  --name RootManageSharedAccessKey \
  --namespace-name crm-app-servicebus \
  --resource-group crm-app-rg \
  --query primaryConnectionString \
  --output tsv
```

## Environment Variables Configuration

Set the following environment variables for your application:

### Required Variables
```bash
# Azure Storage Configuration
export AZURE_STORAGE_CONNECTION_STRING="DefaultEndpointsProtocol=https;AccountName=crmappstorage;AccountKey=...;EndpointSuffix=core.windows.net"
export AZURE_STORAGE_CONTAINER_NAME="crm-files"

# Database Configuration (use Azure Database for MySQL)
export SPRING_DATASOURCE_URL="jdbc:mysql://your-mysql-server.mysql.database.azure.com:3306/crm?useSSL=true&requireSSL=true"
export SPRING_DATASOURCE_USERNAME="your-admin@your-mysql-server"
export SPRING_DATASOURCE_PASSWORD="your-password"
```

### Optional Variables
```bash
# Azure Service Bus Configuration (for scheduled tasks)
export AZURE_SERVICEBUS_CONNECTION_STRING="Endpoint=sb://crm-app-servicebus.servicebus.windows.net/;SharedAccessKeyName=...;SharedAccessKey=..."
export AZURE_SERVICEBUS_QUEUE_NAME="scheduled-tasks"

# Application Timezone (defaults to UTC)
export APP_TIMEZONE="UTC"
```

## Azure App Service Deployment

### Option 1: Deploy to Azure App Service (Linux)

```bash
# Create App Service Plan
az appservice plan create \
  --name crm-app-plan \
  --resource-group crm-app-rg \
  --location eastus \
  --is-linux \
  --sku B2

# Create Web App
az webapp create \
  --name crm-app \
  --resource-group crm-app-rg \
  --plan crm-app-plan \
  --runtime "JAVA:8-jre8"

# Configure environment variables
az webapp config appsettings set \
  --name crm-app \
  --resource-group crm-app-rg \
  --settings \
    AZURE_STORAGE_CONNECTION_STRING="your-connection-string" \
    AZURE_STORAGE_CONTAINER_NAME="crm-files" \
    SPRING_DATASOURCE_URL="your-database-url" \
    SPRING_DATASOURCE_USERNAME="your-username" \
    SPRING_DATASOURCE_PASSWORD="your-password"

# Deploy JAR file
az webapp deploy \
  --name crm-app \
  --resource-group crm-app-rg \
  --src-path target/crm-0.0.1-SNAPSHOT.jar \
  --type jar
```

### Option 2: Deploy to Azure Container Apps

```bash
# Build and push Docker image
docker build -t crmapp:latest .
docker tag crmapp:latest your-registry.azurecr.io/crmapp:latest
docker push your-registry.azurecr.io/crmapp:latest

# Create Container App
az containerapp create \
  --name crm-app \
  --resource-group crm-app-rg \
  --environment crm-app-env \
  --image your-registry.azurecr.io/crmapp:latest \
  --target-port 8080 \
  --ingress external \
  --env-vars \
    AZURE_STORAGE_CONNECTION_STRING="your-connection-string" \
    AZURE_STORAGE_CONTAINER_NAME="crm-files" \
    SPRING_DATASOURCE_URL="your-database-url" \
    SPRING_DATASOURCE_USERNAME="your-username" \
    SPRING_DATASOURCE_PASSWORD="your-password"
```

## Database Migration

### Azure Database for MySQL
Replace the local MySQL instance with Azure Database for MySQL:

```bash
# Create Azure Database for MySQL
az mysql server create \
  --name crm-mysql-server \
  --resource-group crm-app-rg \
  --location eastus \
  --admin-user myadmin \
  --admin-password "YourPassword123!" \
  --sku-name B_Gen5_1 \
  --version 5.7

# Create database
az mysql db create \
  --name crm \
  --server-name crm-mysql-server \
  --resource-group crm-app-rg

# Configure firewall (allow Azure services)
az mysql server firewall-rule create \
  --name AllowAzureServices \
  --server-name crm-mysql-server \
  --resource-group crm-app-rg \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

## Testing Cloud Readiness

### 1. Test Azure Blob Storage Integration
```bash
# Upload a test file to Azure Blob Storage
az storage blob upload \
  --account-name crmappstorage \
  --container-name crm-files \
  --name test.csv \
  --file local-test.csv \
  --connection-string "$AZURE_STORAGE_CONNECTION_STRING"

# Verify the application can read it
curl http://your-app-url/csv/test
```

### 2. Test PDF Generation
```bash
# Generate a PDF (should be stored in Azure Blob Storage)
curl -X POST http://your-app-url/pdf-generator \
  -d "name=test-pdf&content=Hello Azure"

# Verify the PDF is in Azure Blob Storage
az storage blob list \
  --account-name crmappstorage \
  --container-name crm-files \
  --connection-string "$AZURE_STORAGE_CONNECTION_STRING"
```

### 3. Test DateTime Handling
```bash
# Verify timezone-agnostic time handling
curl http://your-app-url/date/test
```

## Security Best Practices

### 1. Use Azure Key Vault for Secrets
Instead of environment variables, use Azure Key Vault:

```bash
# Create Key Vault
az keyvault create \
  --name crm-app-keyvault \
  --resource-group crm-app-rg \
  --location eastus

# Store secrets
az keyvault secret set \
  --vault-name crm-app-keyvault \
  --name "azure-storage-connection-string" \
  --value "your-connection-string"

# Grant App Service access to Key Vault
az webapp identity assign \
  --name crm-app \
  --resource-group crm-app-rg

az keyvault set-policy \
  --name crm-app-keyvault \
  --object-id <app-service-principal-id> \
  --secret-permissions get list
```

### 2. Enable Managed Identity
Use Azure Managed Identity instead of connection strings:

```java
// Update ReadDataUtils.java to use DefaultAzureCredential
BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
    .endpoint("https://crmappstorage.blob.core.windows.net")
    .credential(new DefaultAzureCredentialBuilder().build())
    .buildClient();
```

## Monitoring and Logging

### Enable Application Insights
```bash
# Create Application Insights
az monitor app-insights component create \
  --app crm-app-insights \
  --location eastus \
  --resource-group crm-app-rg

# Get instrumentation key
az monitor app-insights component show \
  --app crm-app-insights \
  --resource-group crm-app-rg \
  --query instrumentationKey \
  --output tsv

# Configure in application.properties
azure.application-insights.instrumentation-key=${APPINSIGHTS_INSTRUMENTATIONKEY}
```

## Troubleshooting

### Issue: "Azure Storage connection string is not configured"
**Solution:** Ensure `AZURE_STORAGE_CONNECTION_STRING` environment variable is set.

### Issue: "Container does not exist"
**Solution:** The application will create the container automatically. Ensure the storage account connection string has proper permissions.

### Issue: "Blob not found"
**Solution:** Upload files to Azure Blob Storage before attempting to read them.

### Issue: Database connection failures
**Solution:** 
- Verify firewall rules allow connections from Azure services
- Check connection string format for Azure Database for MySQL
- Ensure SSL is enabled in the connection string

## Cost Optimization

1. **Storage Account**: Use Standard_LRS for development, Standard_GRS for production
2. **Service Bus**: Use Basic tier for development, Standard for production
3. **App Service**: Use B1 or B2 for development, P1V2+ for production
4. **Database**: Use B_Gen5_1 for development, scale up for production

## Next Steps

1. Set up CI/CD pipeline using Azure DevOps or GitHub Actions
2. Configure auto-scaling for App Service or Container Apps
3. Set up Azure Front Door for global distribution
4. Enable Azure Monitor and Application Insights for observability
5. Implement Azure API Management for API gateway functionality

## Support

For issues or questions:
- Azure Documentation: https://docs.microsoft.com/azure
- Azure Support: https://azure.microsoft.com/support
