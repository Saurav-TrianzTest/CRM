package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
/**
 * Cloud-native date/time controller using java.time API.
 * All timestamps are standardized to UTC to avoid timezone issues in distributed cloud environments.
 */
        // Use Instant for UTC timestamps - cloud-native and timezone-independent
        Instant currentInstant = Instant.now();
        model.addAttribute("timestamp", currentInstant);
        
        // Use ZonedDateTime with UTC for timezone-aware operations
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        

}
