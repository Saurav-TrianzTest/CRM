package crm.controller;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Cloud-ready DateTime Test Controller that uses Azure Service Bus for scheduled operations.
 * Replaces local timer dependencies with Azure Service Bus scheduled message delivery.
 * Uses timezone-agnostic UTC timestamps for distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @Value("${azure.servicebus.connection-string:}")
    private String serviceBusConnectionString;

    @Value("${azure.servicebus.queue-name:scheduled-tasks}")
    private String queueName;

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based timestamps for cloud-native, timezone-agnostic operations
        Instant currentInstant = Instant.now();
        OffsetDateTime currentUtcDateTime = OffsetDateTime.now(ZoneOffset.UTC);
        
        model.addAttribute("timestamp", currentInstant);
        model.addAttribute("utcDateTime", currentUtcDateTime);
        model.addAttribute("utcDate", currentUtcDateTime.toLocalDate());
        model.addAttribute("epochMillis", currentInstant.toEpochMilli());
        
        // Demonstrate Azure Service Bus scheduled message capability
        // This replaces java.util.Timer for distributed, cloud-native scheduling
        if (serviceBusConnectionString != null && !serviceBusConnectionString.isEmpty()) {
            try {
                scheduleTaskWithAzureServiceBus("Sample scheduled task", Duration.ofMinutes(5));
                model.addAttribute("scheduledTaskStatus", "Task scheduled successfully via Azure Service Bus");
            } catch (Exception e) {
                model.addAttribute("scheduledTaskStatus", "Azure Service Bus not configured: " + e.getMessage());
            }
        } else {
            model.addAttribute("scheduledTaskStatus", "Azure Service Bus not configured (set AZURE_SERVICEBUS_CONNECTION_STRING)");
        }
        
        return "date/test";
    }

    /**
     * Schedules a task using Azure Service Bus scheduled message delivery.
     * This replaces java.util.Timer for distributed, timezone-agnostic task execution.
     * 
     * @param taskMessage The message/task to schedule
     * @param delay The delay before the task should be executed
     */
    private void scheduleTaskWithAzureServiceBus(String taskMessage, Duration delay) {
        if (serviceBusConnectionString == null || serviceBusConnectionString.isEmpty()) {
            throw new IllegalStateException("Azure Service Bus connection string is not configured");
        }

        try (ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(serviceBusConnectionString)
                .sender()
                .queueName(queueName)
                .buildClient()) {

            // Calculate scheduled enqueue time in UTC
            OffsetDateTime scheduledEnqueueTime = OffsetDateTime.now(ZoneOffset.UTC).plus(delay);

            // Create a scheduled message
            ServiceBusMessage message = new ServiceBusMessage(taskMessage)
                    .setScheduledEnqueueTime(scheduledEnqueueTime);

            // Send the scheduled message
            senderClient.sendMessage(message);

            System.out.println("Scheduled task via Azure Service Bus: " + taskMessage + 
                             " at " + scheduledEnqueueTime);
        } catch (Exception e) {
            System.err.println("Failed to schedule task via Azure Service Bus: " + e.getMessage());
            throw new RuntimeException("Failed to schedule task via Azure Service Bus", e);
        }
    }

    /**
     * Schedules a recurring task using Azure Service Bus.
     * For recurring tasks, implement a message handler that re-schedules itself after processing.
     * 
     * @param taskMessage The message/task to schedule
     * @param initialDelay Initial delay before first execution
     * @param period Period between executions
     */
    private void scheduleRecurringTaskWithAzureServiceBus(String taskMessage, Duration initialDelay, Duration period) {
        // For recurring tasks, the message handler should:
        // 1. Process the message
        // 2. Schedule the next occurrence by sending a new scheduled message
        // This pattern ensures distributed, stateless recurring task execution
        
        scheduleTaskWithAzureServiceBus(taskMessage + " (recurring, period: " + period + ")", initialDelay);
        
        // Note: The actual recurring logic should be implemented in the message handler/consumer
        // which processes messages from the queue and re-schedules the next occurrence
    }
}
