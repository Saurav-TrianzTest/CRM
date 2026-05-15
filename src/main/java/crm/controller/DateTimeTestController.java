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
import java.time.format.DateTimeFormatter;

/**
 * Cloud-ready DateTime Controller that uses UTC timestamps
 * to avoid timezone inconsistencies in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based timestamps for cloud-native consistency
        Instant utcInstant = Instant.now();
        ZonedDateTime utcZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        LocalDateTime utcLocalDateTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDate utcLocalDate = LocalDate.now(ZoneOffset.UTC);
        
        // Add UTC timestamps to model
        model.addAttribute("standardDate", utcInstant.toString());
        model.addAttribute("localDateTime", utcLocalDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z");
        model.addAttribute("localDate", utcLocalDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("timestamp", utcInstant);
        model.addAttribute("zonedDateTime", utcZonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        
        // Add timezone information for display
        model.addAttribute("timezone", "UTC");
        model.addAttribute("timezoneOffset", "+00:00");
        
        return "date/test";
    }

}
