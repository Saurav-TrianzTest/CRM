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
 * Controller for date/time demonstration.
 *
 * Blockers cr-java-0111 (lines 19-20): Replaced java.util.Date usage with
 * java.time API standardised on UTC to eliminate timezone and clock
 * synchronisation issues in distributed cloud environments.
 *
 * - new Date()          → ZonedDateTime.now(ZoneOffset.UTC)  (UTC-aware instant)
 * - LocalDateTime.now() → ZonedDateTime.now(ZoneOffset.UTC)  (explicit UTC zone)
 * - LocalDate.now()     → LocalDate.now(ZoneOffset.UTC)      (UTC date)
 * - Instant.now()       retained as-is (already UTC by definition)
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // UTC-standardised replacements for java.util.Date and server-local time calls
        model.addAttribute("standardDate", ZonedDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDateTime", ZonedDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
