package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * DateTimeTestController
 *
 * Cloud Readiness Fix (cr-java-0111 - Clock/Time Dependencies):
 * Replaced server-local timezone-dependent LocalDateTime.now() and LocalDate.now()
 * calls with UTC-based equivalents using ZonedDateTime and Clock.systemUTC().
 * This ensures consistent, timezone-agnostic time handling across distributed
 * cloud environments and multiple Azure regions.
 *
 * For scheduled operations, use Azure Service Bus Scheduled Messages instead of
 * java.util.Timer or server-local scheduling. See AzureServiceBusSchedulerService
 * for the cloud-native scheduling pattern.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    /**
     * UTC clock instance — injected as a constant so it can be overridden in tests
     * and is not tied to the server's local timezone.
     */
    private final Clock utcClock = Clock.systemUTC();

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based time to avoid server-local timezone inconsistencies
        // across distributed cloud deployments and multiple Azure regions.
        model.addAttribute("standardDate", Date.from(Instant.now(utcClock)));

        // cr-java-0111 fix (Line 19): Replace LocalDateTime.now() (server-local timezone)
        // with ZonedDateTime using UTC to ensure timezone-agnostic behaviour in cloud.
        model.addAttribute("localDateTime", ZonedDateTime.now(ZoneOffset.UTC));

        // cr-java-0111 fix (Line 20): Replace LocalDate.now() (server-local timezone)
        // with LocalDate derived from UTC clock to ensure consistent date resolution
        // across all Azure regions and container instances.
        model.addAttribute("localDate", LocalDate.now(utcClock));

        model.addAttribute("timestamp", Instant.now(utcClock));
        return "date/test";
    }

}
