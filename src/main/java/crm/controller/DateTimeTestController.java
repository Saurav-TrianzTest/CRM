package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Cloud-native date/time controller using java.time API.
 * All timestamps are standardized to UTC to ensure consistency across distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use java.time API instead of java.util.Date for cloud-native time handling
        // All times are in UTC to avoid timezone issues in distributed cloud deployments
        
        Instant utcInstant = Instant.now();
        ZonedDateTime utcZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        LocalDateTime utcLocalDateTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDate utcLocalDate = LocalDate.now(ZoneOffset.UTC);
        
        // Add UTC-based timestamps to model
        model.addAttribute("utcInstant", utcInstant);
        model.addAttribute("utcZonedDateTime", utcZonedDateTime);
        model.addAttribute("localDateTime", utcLocalDateTime);
        model.addAttribute("localDate", utcLocalDate);
        model.addAttribute("timestamp", utcInstant);
        
        // For backward compatibility, if needed, convert to epoch milliseconds
        model.addAttribute("epochMillis", utcInstant.toEpochMilli());
        
        return "date/test";
    }

}
