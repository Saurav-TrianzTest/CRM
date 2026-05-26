package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cloud-ready DateTime controller using java.time API with UTC standardization.
 * 
 * This implementation eliminates timezone and clock synchronization issues by:
 * 1. Using java.time API instead of legacy java.util.Date
 * 2. Standardizing on UTC for all time operations
 * 3. Using Clock abstraction for testability and consistency
 * 4. Providing explicit timezone handling for display purposes
 * 
 * Best practices for distributed cloud environments:
 * - Store all timestamps in UTC
 * - Use Instant for machine timestamps
 * - Use ZonedDateTime only when timezone context is required
 * - Convert to local timezone only at presentation layer
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    /**
     * System clock using UTC timezone.
     * This ensures consistent time handling across all container instances
     * regardless of their deployment region or local timezone settings.
     */
    private final Clock clock = Clock.systemUTC();
    
    /**
     * UTC timezone constant for explicit timezone handling.
     */
    private static final ZoneId UTC = ZoneId.of("UTC");
    
    /**
     * ISO 8601 formatter for standardized timestamp representation.
     */
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use Instant for machine timestamps (always UTC, no timezone ambiguity)
        Instant currentInstant = Instant.now(clock);
        
        // Use ZonedDateTime with explicit UTC timezone for cloud environments
        ZonedDateTime utcDateTime = ZonedDateTime.now(clock);
        
        // LocalDateTime derived from UTC for consistency
        LocalDateTime localDateTime = LocalDateTime.now(clock);
        
        // LocalDate derived from UTC
        LocalDate localDate = LocalDate.now(clock);
        
        // Add UTC-based timestamps to model
        model.addAttribute("timestamp", currentInstant);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        
        // Add formatted ISO 8601 timestamp for display
        model.addAttribute("isoTimestamp", ISO_FORMATTER.format(currentInstant));
        
        // Add timezone information for transparency
        model.addAttribute("timezone", "UTC");
        model.addAttribute("zoneId", UTC.getId());
        
        // Example: Convert to different timezone for display (if needed)
        // This should only be done at presentation layer, never for storage/logic
        ZonedDateTime easternTime = utcDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        model.addAttribute("easternDateTime", easternTime);
        
        return "date/test";
    }
    
    /**
     * Example method demonstrating UTC-based timestamp generation for logging/auditing.
     * All timestamps stored in database or logs should use this pattern.
     * 
     * @return UTC timestamp as Instant
     */
    public Instant getCurrentUtcTimestamp() {
        return Instant.now(clock);
    }
    
    /**
     * Example method for parsing ISO 8601 timestamps from external systems.
     * 
     * @param isoTimestamp ISO 8601 formatted timestamp string
     * @return Instant representing the timestamp
     */
    public Instant parseIsoTimestamp(String isoTimestamp) {
        return Instant.parse(isoTimestamp);
    }
    
    /**
     * Example method for converting Instant to ZonedDateTime with explicit timezone.
     * Use this pattern when timezone context is required for business logic.
     * 
     * @param instant the instant to convert
     * @param zoneId the target timezone
     * @return ZonedDateTime in the specified timezone
     */
    public ZonedDateTime toZonedDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId);
    }

}
