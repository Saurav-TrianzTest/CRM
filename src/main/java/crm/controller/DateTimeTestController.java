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
 * DateTimeTestController demonstrates cloud-safe time handling.
 * All time values are standardized on UTC using the java.time API
 * (Instant, ZonedDateTime, LocalDateTime) to eliminate timezone and
 * clock synchronization issues across distributed cloud environments.
 * java.util.Date has been replaced to avoid server-local timezone dependencies.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Standardized UTC instant — replaces new Date() which relied on server-local timezone
        Instant utcInstant = Instant.now();
        // ZonedDateTime explicitly set to UTC for consistent cross-region behavior
        ZonedDateTime utcZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        model.addAttribute("standardDate", utcZonedDateTime);
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", utcInstant);
        return "date/test";
    }

}
