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
 * Blocker cr-java-0111 (lines 19-20): Replaced {@code new java.util.Date()} and
 * {@code LocalDateTime.now()} with the {@code java.time} API standardised on UTC.
 *
 * <ul>
 *   <li>{@code new Date()} → {@code ZonedDateTime.now(ZoneOffset.UTC)} — explicit UTC
 *       ZonedDateTime avoids server-local timezone dependency.</li>
 *   <li>{@code LocalDateTime.now()} → {@code ZonedDateTime.now(ZoneOffset.UTC)} — same
 *       UTC-anchored replacement; LocalDateTime has no timezone context and is therefore
 *       unsafe in distributed cloud deployments.</li>
 * </ul>
 *
 * All time values are now expressed in UTC, ensuring consistent behaviour across
 * cloud regions, containers, and inter-service communication.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Replaced: new Date()  →  ZonedDateTime.now(ZoneOffset.UTC)
        model.addAttribute("standardDate", ZonedDateTime.now(ZoneOffset.UTC));
        // Replaced: LocalDateTime.now()  →  ZonedDateTime.now(ZoneOffset.UTC)
        model.addAttribute("localDateTime", ZonedDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
