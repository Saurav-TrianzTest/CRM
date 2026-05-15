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
 * Cloud-ready DateTime controller that uses java.time API and standardizes on UTC
 * to eliminate timezone and clock synchronization issues in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    // Use UTC as the standard timezone for all cloud operations
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final Clock UTC_CLOCK = Clock.systemUTC();
    
    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use Instant for UTC timestamps (recommended for cloud environments)
        Instant currentInstant = Instant.now(UTC_CLOCK);
        
        // Use ZonedDateTime with explicit UTC timezone
        ZonedDateTime utcDateTime = ZonedDateTime.now(UTC_CLOCK);
        
        // LocalDateTime should be used with explicit timezone awareness
        LocalDateTime localDateTime = LocalDateTime.now(UTC_CLOCK);
        
        // LocalDate with UTC clock
        LocalDate localDate = LocalDate.now(UTC_CLOCK);
        
        // Format timestamps in ISO-8601 format for consistency
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_INSTANT;
        String formattedTimestamp = isoFormatter.format(currentInstant);
        
        // Add attributes to model
        model.addAttribute("timestamp", currentInstant);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("formattedTimestamp", formattedTimestamp);
        model.addAttribute("timezone", "UTC");
        
        // For logging and debugging
        model.addAttribute("clockInfo", "Using UTC Clock for cloud consistency");
        
        return "date/test";
    }
    
    /**
     * Example method showing how to convert between timezones in cloud environments.
     * Always store in UTC, convert to local timezone only for display purposes.
     */
    public ZonedDateTime convertToUserTimezone(Instant utcInstant, String userTimezone) {
        ZoneId userZone = ZoneId.of(userTimezone);
        return utcInstant.atZone(userZone);
    }
    
    /**
     * Example method for scheduled operations in cloud environments.
     * Use Instant and Duration for scheduling instead of java.util.Timer.
     */
    public Instant calculateNextScheduledTime(Instant baseTime, long durationSeconds) {
        return baseTime.plusSeconds(durationSeconds);
    }

}
