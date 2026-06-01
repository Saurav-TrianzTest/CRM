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
 * Uses java.time API with UTC standardization instead of java.util.Date
 * to ensure consistent behavior across distributed cloud environments and regions.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Standardized on UTC using java.time API — eliminates server-local timezone dependencies
        Instant nowUtc = Instant.now();
        ZonedDateTime zonedDateTimeUtc = ZonedDateTime.now(ZoneOffset.UTC);
        LocalDate localDateUtc = LocalDate.now(ZoneOffset.UTC);

        model.addAttribute("standardDate", zonedDateTimeUtc);
        model.addAttribute("localDateTime", zonedDateTimeUtc.toLocalDateTime());
        model.addAttribute("localDate", localDateUtc);
        model.addAttribute("timestamp", nowUtc);
        return "date/test";
    }

}
