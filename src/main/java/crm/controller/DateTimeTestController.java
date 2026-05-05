package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;

/**
 * Cloud-ready date/time controller using java.time API with UTC standardization.
 * Eliminates timezone and clock synchronization issues in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use Instant for UTC timestamps - cloud-native best practice
        Instant utcInstant = Instant.now();
        
        // Use ZonedDateTime with UTC for timezone-aware operations
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        
        // LocalDateTime and LocalDate for timezone-independent operations
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
        
        // Add UTC-based timestamps to model
        model.addAttribute("utcInstant", utcInstant);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("timestamp", utcInstant);
        
        return "date/test";
    }

}
