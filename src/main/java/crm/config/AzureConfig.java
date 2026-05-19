package crm.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Azure Cloud Services Configuration.
 * Configures Azure Blob Storage and Azure Service Bus clients as Spring beans.
 */
@Configuration
public class AzureConfig {

    @Value("${azure.storage.connection-string:}")
    private String storageConnectionString;

    @Value("${azure.storage.container-name:crm-files}")
    private String containerName;

    @Value("${azure.servicebus.connection-string:}")
    private String serviceBusConnectionString;

    @Value("${azure.servicebus.queue-name:scheduled-tasks}")
    private String queueName;

    /**
     * Creates a BlobServiceClient bean for Azure Blob Storage operations.
     * Returns null if connection string is not configured.
     */
    @Bean
    public BlobServiceClient blobServiceClient() {
        if (storageConnectionString == null || storageConnectionString.isEmpty()) {
            System.out.println("WARNING: Azure Storage connection string not configured. " +
                             "Set AZURE_STORAGE_CONNECTION_STRING environment variable.");
            return null;
        }

        try {
            BlobServiceClient client = new BlobServiceClientBuilder()
                    .connectionString(storageConnectionString)
                    .buildClient();
            
            System.out.println("Azure Blob Storage client initialized successfully");
            return client;
        } catch (Exception e) {
            System.err.println("Failed to initialize Azure Blob Storage client: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a BlobContainerClient bean for the configured container.
     * Returns null if BlobServiceClient is not available.
     */
    @Bean
    public BlobContainerClient blobContainerClient(BlobServiceClient blobServiceClient) {
        if (blobServiceClient == null) {
            return null;
        }

        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            // Create container if it doesn't exist
            if (!containerClient.exists()) {
                containerClient.create();
                System.out.println("Created Azure Blob Storage container: " + containerName);
            } else {
                System.out.println("Using existing Azure Blob Storage container: " + containerName);
            }
            
            return containerClient;
        } catch (Exception e) {
            System.err.println("Failed to initialize Azure Blob Container client: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a ServiceBusSenderClient bean for Azure Service Bus operations.
     * Returns null if connection string is not configured.
     */
    @Bean
    public ServiceBusSenderClient serviceBusSenderClient() {
        if (serviceBusConnectionString == null || serviceBusConnectionString.isEmpty()) {
            System.out.println("WARNING: Azure Service Bus connection string not configured. " +
                             "Set AZURE_SERVICEBUS_CONNECTION_STRING environment variable.");
            return null;
        }

        try {
            ServiceBusSenderClient client = new ServiceBusClientBuilder()
                    .connectionString(serviceBusConnectionString)
                    .sender()
                    .queueName(queueName)
                    .buildClient();
            
            System.out.println("Azure Service Bus sender client initialized successfully for queue: " + queueName);
            return client;
        } catch (Exception e) {
            System.err.println("Failed to initialize Azure Service Bus client: " + e.getMessage());
            return null;
        }
    }
}
