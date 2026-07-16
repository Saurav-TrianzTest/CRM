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
 * All time values are now sourced from the java.time API and standardised on UTC
 * (ZoneOffset.UTC / ZonedDateTime with UTC zone) to eliminate server-local timezone
 * dependencies that cause scheduling failures and time-related logic errors in
 * distributed cloud environments.
 *
 * java.util.Date and java.util.Calendar have been removed; java.time.Instant,
 * java.time.ZonedDateTime, and java.time.LocalDate (UTC) are used instead.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // UTC instant — replaces new Date() (blocker-4, line 19)
        Instant nowUtc = Instant.now();

        // UTC ZonedDateTime — replaces LocalDateTime.now() (blocker-5, line 20)
        // LocalDateTime.now() is timezone-ambiguous; ZonedDateTime with UTC is explicit.
        ZonedDateTime zonedDateTimeUtc = ZonedDateTime.now(ZoneOffset.UTC);

        // UTC date — derived from the UTC instant so it is timezone-consistent
        LocalDate localDateUtc = LocalDate.now(ZoneOffset.UTC);

        model.addAttribute("standardDate", nowUtc.toString());
        model.addAttribute("localDateTime", zonedDateTimeUtc);
        model.addAttribute("localDate", localDateUtc);
        model.addAttribute("timestamp", nowUtc);
        return "date/test";
    }

}
