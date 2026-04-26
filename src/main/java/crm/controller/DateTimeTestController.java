package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Cloud-ready DateTime controller using java.time API with UTC standardization.
 * Eliminates timezone inconsistencies in distributed cloud environments.
 * 
 * Migrated from java.util.Date to java.time API for cloud compatibility.
 * All timestamps use UTC to ensure consistency across cloud regions and containers.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC for all timestamps to ensure consistency across cloud regions
        Instant now = Instant.now();
        model.addAttribute("standardDate", now);
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", now);
        model.addAttribute("zonedDateTime", ZonedDateTime.now(ZoneOffset.UTC));
        return "date/test";
    }

}
