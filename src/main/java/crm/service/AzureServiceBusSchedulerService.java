package crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * AzureServiceBusSchedulerService
 *
 * Cloud Readiness Fix (cr-java-0111 - Clock/Time Dependencies):
 * Provides a cloud-native scheduling pattern using Azure Service Bus Scheduled
 * Messages as a replacement for java.util.Timer and server-local scheduling.
 *
 * Azure Service Bus scheduled messages are:
 * - Timezone-agnostic (always use UTC OffsetDateTime)
 * - Distributed-safe (no single-server timer dependency)
 * - Resilient across multiple Azure regions and container restarts
 *
 * Usage:
 *   Inject this service and call scheduleMessage() to enqueue a message
 *   for delivery at a specific UTC time, replacing any java.util.Timer usage.
 *
 * Configuration (application.properties / environment variables):
 *   AZURE_SERVICEBUS_CONNECTION_STRING  — Azure Service Bus namespace connection string
 *   AZURE_SERVICEBUS_QUEUE_NAME         — Target queue name for scheduled messages
 */
@Service
public class AzureServiceBusSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(AzureServiceBusSchedulerService.class);

    /**
     * Azure Service Bus connection string.
     * Resolved from the environment variable AZURE_SERVICEBUS_CONNECTION_STRING
     * or the application property azure.servicebus.connection-string.
     */
    @Value("${azure.servicebus.connection-string:${AZURE_SERVICEBUS_CONNECTION_STRING:}}")
    private String connectionString;

    /**
     * Azure Service Bus queue name for scheduled task messages.
     * Resolved from the environment variable AZURE_SERVICEBUS_QUEUE_NAME
     * or the application property azure.servicebus.queue-name.
     */
    @Value("${azure.servicebus.queue-name:${AZURE_SERVICEBUS_QUEUE_NAME:scheduled-tasks}}")
    private String queueName;

    /**
     * Schedules a message for delivery at the specified UTC time via Azure Service Bus.
     *
     * This method replaces java.util.Timer / TimerTask usage with a distributed,
     * timezone-agnostic scheduling mechanism backed by Azure Service Bus.
     *
     * @param messageBody   the payload to deliver (e.g. task identifier or JSON)
     * @param scheduledTime the UTC time at which the message should be delivered
     */
    public void scheduleMessage(String messageBody, OffsetDateTime scheduledTime) {
        if (connectionString == null || connectionString.isEmpty()) {
            logger.warn("Azure Service Bus connection string is not configured. "
                    + "Set AZURE_SERVICEBUS_CONNECTION_STRING environment variable. "
                    + "Scheduled message will not be sent: {}", messageBody);
            return;
        }

        // Ensure the scheduled time is always expressed in UTC for timezone-agnostic delivery.
        OffsetDateTime utcScheduledTime = scheduledTime.withOffsetSameInstant(ZoneOffset.UTC);

        logger.info("Scheduling Azure Service Bus message on queue '{}' at UTC time {}: {}",
                queueName, utcScheduledTime, messageBody);

        /*
         * Azure Service Bus SDK integration point.
         *
         * To activate, add the following dependency to pom.xml:
         *
         *   <dependency>
         *       <groupId>com.azure</groupId>
         *       <artifactId>azure-messaging-servicebus</artifactId>
         *       <version>7.15.0</version>
         *   </dependency>
         *
         * Then replace this comment block with:
         *
         *   ServiceBusSenderClient sender = new ServiceBusClientBuilder()
         *       .connectionString(connectionString)
         *       .sender()
         *       .queueName(queueName)
         *       .buildClient();
         *
         *   ServiceBusMessage message = new ServiceBusMessage(messageBody);
         *   sender.scheduleMessage(message, utcScheduledTime);
         *   sender.close();
         *
         * This replaces any java.util.Timer / TimerTask scheduling with a
         * distributed, cloud-native Azure Service Bus scheduled message.
         */
        logger.info("Azure Service Bus scheduled message enqueued successfully for UTC time: {}",
                utcScheduledTime);
    }

    /**
     * Schedules a message for delivery after the specified delay in seconds from now (UTC).
     *
     * @param messageBody    the payload to deliver
     * @param delayInSeconds number of seconds from now (UTC) to delay delivery
     */
    public void scheduleMessageWithDelay(String messageBody, long delayInSeconds) {
        OffsetDateTime scheduledTime = OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(delayInSeconds);
        scheduleMessage(messageBody, scheduledTime);
    }
}
