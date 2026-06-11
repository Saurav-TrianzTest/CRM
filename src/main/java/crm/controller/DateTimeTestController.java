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
 * Controller for date/time testing.
 * Migrated from java.util.Date to java.time API (Instant, ZonedDateTime) and
 * standardized on UTC for all time-sensitive operations to eliminate timezone
 * and clock synchronization issues in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Replaced java.util.Date with java.time.Instant (UTC) for cloud-safe time handling
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
        model.addAttribute("standardDate", utcNow);
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
