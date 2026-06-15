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
 * Replaced java.util.Date usage with java.time API (ZonedDateTime / Instant)
 * standardized on UTC to eliminate timezone inconsistencies across distributed
 * cloud environments and containers.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Replaced new Date() (server-local timezone) with ZonedDateTime in UTC
        model.addAttribute("standardDate", ZonedDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
