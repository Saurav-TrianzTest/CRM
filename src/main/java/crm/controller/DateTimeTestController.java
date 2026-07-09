package crm.controller;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Controller for date/time demonstration and scheduled task dispatch.
 *
 * Blocker-4 & Blocker-5 fix:
 *   - Replaced java.util.Date (server-local timezone) with UTC-based java.time types
 *     (Instant, LocalDateTime.now(ZoneOffset.UTC), LocalDate.now(ZoneOffset.UTC))
 *     to ensure consistent time handling across distributed cloud deployments.
 *   - Replaced java.util.Timer-based local scheduling with Azure Service Bus
 *     scheduled message delivery for distributed, timezone-agnostic task execution.
 *
 * Environment variables required for Azure Service Bus:
 *   AZURE_SERVICE_BUS_CONNECTION_STRING – Service Bus namespace connection string
 *   AZURE_SERVICE_BUS_QUEUE_NAME        – target queue or topic name
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeTestController.class);

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based time sources to avoid server-local timezone dependencies
        Instant nowUtc = Instant.now();
        LocalDateTime localDateTimeUtc = LocalDateTime.now(ZoneOffset.UTC);
        LocalDate localDateUtc = LocalDate.now(ZoneOffset.UTC);

        model.addAttribute("standardDate", nowUtc.toString());
        model.addAttribute("localDateTime", localDateTimeUtc);
        model.addAttribute("localDate", localDateUtc);
        model.addAttribute("timestamp", nowUtc);
        return "date/test";
    }

    /**
     * Schedules a task message on Azure Service Bus instead of using java.util.Timer.
     * This ensures distributed, timezone-agnostic task execution across cloud instances.
     *
     * @param messageBody   the task payload to schedule
     * @param scheduledTime the UTC time at which the message should be delivered
     */
    public void scheduleTask(String messageBody, OffsetDateTime scheduledTime) {
        String connectionString = System.getenv("AZURE_SERVICE_BUS_CONNECTION_STRING");
        String queueName        = System.getenv("AZURE_SERVICE_BUS_QUEUE_NAME");

        if (connectionString == null || connectionString.isEmpty()) {
            logger.error("Environment variable AZURE_SERVICE_BUS_CONNECTION_STRING is not set.");
            return;
        }
        if (queueName == null || queueName.isEmpty()) {
            logger.error("Environment variable AZURE_SERVICE_BUS_QUEUE_NAME is not set.");
            return;
        }

        try (ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient()) {

            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            long sequenceNumber = senderClient.scheduleMessage(message, scheduledTime);
            logger.info("Task scheduled on Azure Service Bus queue '{}' at '{}'. Sequence number: {}",
                    queueName, scheduledTime, sequenceNumber);

        } catch (Exception e) {
            logger.error("Failed to schedule task on Azure Service Bus: {}", e.getMessage(), e);
        }
    }

}
