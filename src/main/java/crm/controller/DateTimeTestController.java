package crm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Cloud-ready DateTime Test Controller.
 * Uses timezone-agnostic time handling for distributed cloud environments.
 * Replaces java.util.Date and java.util.Timer with cloud-native patterns.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @Value("${app.timezone:UTC}")
    private String applicationTimezone;

    /**
     * Displays date/time information using cloud-native, timezone-agnostic patterns.
     * All times are stored and processed in UTC, then converted to the configured timezone for display.
     */
    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use Instant for timezone-agnostic timestamps (recommended for cloud)
        Instant currentInstant = Instant.now();
        
        // Get the configured timezone (default to UTC for cloud environments)
        ZoneId zoneId = ZoneId.of(applicationTimezone);
        
        // Convert to ZonedDateTime for timezone-aware operations
        ZonedDateTime zonedDateTime = currentInstant.atZone(zoneId);
        
        // Use LocalDateTime and LocalDate for display purposes only
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        LocalDate localDate = zonedDateTime.toLocalDate();
        
        // Add attributes to model
        model.addAttribute("timestamp", currentInstant);
        model.addAttribute("zonedDateTime", zonedDateTime);
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("timezone", applicationTimezone);
        model.addAttribute("utcTime", currentInstant.toString());
        
        return "date/test";
    }
}

/**
 * MIGRATION NOTES FOR SCHEDULED TASKS:
 * 
 * For scheduled operations that previously used java.util.Timer, migrate to Azure Service Bus Scheduled Messages:
 * 
 * 1. Add Azure Service Bus dependency to pom.xml:
 *    <dependency>
 *        <groupId>com.azure</groupId>
 *        <artifactId>azure-messaging-servicebus</artifactId>
 *        <version>7.13.0</version>
 *    </dependency>
 * 
 * 2. Configure Azure Service Bus connection in application.properties:
 *    azure.servicebus.connection-string=${AZURE_SERVICEBUS_CONNECTION_STRING}
 *    azure.servicebus.queue-name=scheduled-tasks
 * 
 * 3. Example scheduled message service:
 * 
 * @Service
 * public class ScheduledTaskService {
 *     
 *     @Value("${azure.servicebus.connection-string}")
 *     private String connectionString;
 *     
 *     @Value("${azure.servicebus.queue-name}")
 *     private String queueName;
 *     
 *     public void scheduleTask(String taskData, Duration delay) {
 *         ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
 *             .connectionString(connectionString)
 *             .sender()
 *             .queueName(queueName)
 *             .buildClient();
 *         
 *         ServiceBusMessage message = new ServiceBusMessage(taskData);
 *         message.setScheduledEnqueueTime(OffsetDateTime.now().plus(delay));
 *         
 *         senderClient.sendMessage(message);
 *         senderClient.close();
 *     }
 * }
 * 
 * 4. For recurring tasks, use Azure Functions with Timer Trigger or Azure Logic Apps
 * 
 * BENEFITS:
 * - Distributed scheduling across multiple instances
 * - Timezone-agnostic execution
 * - Automatic retry and dead-letter handling
 * - Scalable and reliable in cloud environments
 * - No dependency on local server timers
 */
