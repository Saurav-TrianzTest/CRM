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
 * Cloud-ready date/time controller using java.time API.
 * Standardized on UTC to eliminate timezone inconsistencies in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    // Use UTC clock for all time operations to ensure consistency across cloud regions
    private static final Clock UTC_CLOCK = Clock.systemUTC();
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use java.time API with UTC standardization for cloud compatibility
        Instant currentInstant = Instant.now(UTC_CLOCK);
        ZonedDateTime utcDateTime = ZonedDateTime.now(UTC_CLOCK);
        LocalDateTime localDateTime = LocalDateTime.now(UTC_CLOCK);
        LocalDate localDate = LocalDate.now(UTC_CLOCK);
        
        // Add formatted timestamps for display
        model.addAttribute("instant", currentInstant);
        model.addAttribute("instantFormatted", ISO_FORMATTER.format(currentInstant));
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("timestamp", currentInstant.toEpochMilli());
        model.addAttribute("timezone", "UTC");
        
        // For backward compatibility, provide ISO-8601 formatted string
        model.addAttribute("isoTimestamp", currentInstant.toString());
        
        return "date/test";
    }

    /**
     * Helper method to get current UTC instant.
     * Use this throughout the application for consistent time handling.
     */
    public static Instant getCurrentUtcInstant() {
        return Instant.now(UTC_CLOCK);
    }

    /**
     * Helper method to get current UTC ZonedDateTime.
     * Use this when timezone information is needed.
     */
    public static ZonedDateTime getCurrentUtcDateTime() {
        return ZonedDateTime.now(UTC_CLOCK);
    }

    /**
     * Helper method to convert epoch milliseconds to UTC Instant.
     * Use this for database timestamp conversions.
     */
    public static Instant fromEpochMillis(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis);
    }

    /**
     * Helper method to format Instant as ISO-8601 string.
     * Use this for API responses and logging.
     */
    public static String formatInstant(Instant instant) {
        return ISO_FORMATTER.format(instant);
    }
}
