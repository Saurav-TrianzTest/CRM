package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Cloud-ready DateTime controller that standardizes on UTC timestamps
 * to eliminate server-local timezone dependencies in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based timestamps for cloud environments
        // This ensures consistency across multiple regions and containers
        
        // Current UTC instant (recommended for cloud applications)
        Instant utcInstant = Instant.now();
        model.addAttribute("timestamp", utcInstant);
        
        // UTC-based date and time
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        model.addAttribute("utcDateTime", utcDateTime);
        
        // UTC date
        LocalDate utcDate = LocalDate.now(ZoneOffset.UTC);
        model.addAttribute("utcDate", utcDate);
        
        // UTC LocalDateTime (for backward compatibility)
        LocalDateTime utcLocalDateTime = LocalDateTime.now(ZoneOffset.UTC);
        model.addAttribute("localDateTime", utcLocalDateTime);
        
        // Legacy Date object (converted from UTC instant)
        Date standardDate = Date.from(utcInstant);
        model.addAttribute("standardDate", standardDate);
        
        // Add timezone information for display purposes
        model.addAttribute("timezone", "UTC");
        model.addAttribute("zoneId", ZoneOffset.UTC.getId());
        
        // Optional: Add user's timezone context if needed for display
        // This should be stored separately and not used for business logic
        String userTimezone = System.getenv().getOrDefault("USER_TIMEZONE", "UTC");
        ZonedDateTime userLocalTime = utcDateTime.withZoneSameInstant(ZoneId.of(userTimezone));
        model.addAttribute("userLocalTime", userLocalTime);
        model.addAttribute("userTimezone", userTimezone);
        
        return "date/test";
    }

}
