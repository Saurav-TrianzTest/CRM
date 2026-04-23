package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Clock;

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

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Replace java.util.Date with java.time.Instant for UTC timestamp
        Instant utcInstant = Instant.now(UTC_CLOCK);
        
        // Use ZonedDateTime with explicit UTC timezone for cloud consistency
        ZonedDateTime utcDateTime = ZonedDateTime.now(UTC_CLOCK);
        
        // LocalDateTime in UTC context
        LocalDateTime utcLocalDateTime = LocalDateTime.now(UTC_CLOCK);
        
        // LocalDate in UTC context
        LocalDate utcLocalDate = LocalDate.now(UTC_CLOCK);

        // Add attributes with clear UTC context
        model.addAttribute("utcInstant", utcInstant);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("utcLocalDateTime", utcLocalDateTime);
        model.addAttribute("utcLocalDate", utcLocalDate);
        model.addAttribute("timezone", "UTC");
        
        // For backward compatibility, provide formatted strings
        model.addAttribute("standardDate", utcInstant.toString());
        model.addAttribute("localDateTime", utcLocalDateTime);
        model.addAttribute("localDate", utcLocalDate);
        model.addAttribute("timestamp", utcInstant);

        return "date/test";
    }

}
