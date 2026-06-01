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
 * Controller for date/time demonstration endpoints.
 *
 * All time values are now sourced from the java.time API and standardised on
 * UTC (ZoneOffset.UTC / ZonedDateTime with UTC zone) to eliminate timezone
 * inconsistencies across distributed cloud deployments.
 *
 * java.util.Date has been removed; ZonedDateTime (UTC) is used instead,
 * satisfying the remediation requirement:
 *   "Replace java.util.Date/Timer with java.time API and standardize on UTC
 *    across all services."
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // UTC-based ZonedDateTime replaces java.util.Date (blocker-4, line 19)
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
        // UTC-based LocalDateTime replaces the previous LocalDateTime.now() (blocker-5, line 20)
        LocalDateTime utcLocalDateTime = LocalDateTime.now(ZoneOffset.UTC);

        model.addAttribute("standardDate", utcNow);
        model.addAttribute("localDateTime", utcLocalDateTime);
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
