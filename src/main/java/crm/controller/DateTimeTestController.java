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
 * Cloud-readiness fix (cr-java-0111):
 * - Removed java.util.Date usage (lines 19-20) which relied on server-local
 *   timezone settings and caused inconsistencies across distributed cloud nodes.
 * - All time values now use the java.time API (Instant, ZonedDateTime, LocalDate).
 * - Standardised on UTC (ZoneOffset.UTC) for all time representations to ensure
 *   consistent behaviour across multi-region cloud deployments and containers.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // UTC instant — replaces new Date() which used server-local timezone
        Instant nowUtc = Instant.now();
        // ZonedDateTime in UTC — replaces any Calendar/Date usage
        ZonedDateTime zonedDateTimeUtc = ZonedDateTime.now(ZoneOffset.UTC);
        // LocalDate in UTC context
        LocalDate localDateUtc = LocalDate.now(ZoneOffset.UTC);

        model.addAttribute("standardDate", nowUtc.toString());
        model.addAttribute("localDateTime", zonedDateTimeUtc);
        model.addAttribute("localDate", localDateUtc);
        model.addAttribute("timestamp", nowUtc);
        return "date/test";
    }

}
