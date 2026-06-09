package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Controller for date/time demonstration endpoints.
 *
 * All time values are standardised on UTC (ZoneOffset.UTC / ZonedDateTime with UTC zone)
 * to avoid timezone inconsistencies across distributed cloud deployments.
 * java.util.Date has been replaced with java.time API types throughout.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // UTC instant – replaces new Date() (blocker-4, line 19)
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
        model.addAttribute("standardDate", utcNow);

        // UTC-based date-time – replaces LocalDateTime.now() (blocker-5, line 20)
        // LocalDateTime.now() is ambiguous in multi-region deployments; ZonedDateTime with UTC is explicit.
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        model.addAttribute("localDateTime", utcDateTime);

        // UTC local date
        LocalDate utcDate = LocalDate.now(ZoneOffset.UTC);
        model.addAttribute("localDate", utcDate);

        // UTC Instant for inter-service communication / logging
        Instant timestamp = Instant.now();
        model.addAttribute("timestamp", timestamp);

        return "date/test";
    }
}
