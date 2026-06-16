package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.ZonedDateTime;
@RequestMapping("/date")
        // Use java.time API with UTC timezone for cloud-native consistency
        // Eliminates timezone and clock synchronization issues across distributed cloud environments
        Instant currentInstant = Instant.now();
        ZonedDateTime utcDateTime = ZonedDateTime.now(java.time.ZoneOffset.UTC);
        
        model.addAttribute("standardDate", currentInstant); // Replaced java.util.Date with Instant
        model.addAttribute("localDateTime", utcDateTime); // Using UTC ZonedDateTime instead of LocalDateTime
        model.addAttribute("localDate", LocalDate.now(java.time.ZoneOffset.UTC)); // UTC-based LocalDate
        model.addAttribute("utcDateTime", utcDateTime);
    }

}
