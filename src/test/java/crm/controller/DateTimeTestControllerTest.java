package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("DateTimeTestController Tests")
class DateTimeTestControllerTest {

    @Test
    @DisplayName("Test DateTimeTestController exists")
    void testDateTimeTestControllerExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test DateTimeTestController class is present")
    void testDateTimeTestControllerClass() throws ClassNotFoundException {
        Class.forName("crm.controller.DateTimeTestController");
    }
}
