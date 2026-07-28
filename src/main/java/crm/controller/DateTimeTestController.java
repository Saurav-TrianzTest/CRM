package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;

/**
 * Cloud-native date/time controller using java.time API.
 * All timestamps are standardized to UTC for cloud consistency.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based timestamps for cloud consistency
        Instant now = Instant.now();
        model.addAttribute("standardDate", ZonedDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("timestamp", now);
        model.addAttribute("utcDateTime", ZonedDateTime.ofInstant(now, ZoneOffset.UTC));
        return "date/test";
    }

}
