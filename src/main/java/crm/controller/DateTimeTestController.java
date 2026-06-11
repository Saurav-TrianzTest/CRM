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
 * Controller for date/time demonstration.
 * Uses java.time API exclusively and standardizes on UTC to avoid timezone
 * inconsistencies across distributed cloud environments and containers.
 * java.util.Date has been replaced with ZonedDateTime (UTC) per cloud readiness requirements.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Replaced java.util.Date with ZonedDateTime in UTC to standardize time across
        // distributed cloud environments and avoid server-local timezone dependencies.
        model.addAttribute("standardDate", ZonedDateTime.now(ZoneOffset.UTC));
        // Replaced LocalDateTime.now() with LocalDateTime.now(ZoneOffset.UTC) to ensure
        // consistent UTC-based time across all cloud regions and container instances.
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
