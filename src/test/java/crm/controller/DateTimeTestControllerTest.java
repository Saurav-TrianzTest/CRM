package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DateTimeTestControllerTest {

    @Test
    void testControllerClassExists() {
        assertNotNull(DateTimeTestController.class);
    }

    @Test
    void testControllerInstantiation() {
        DateTimeTestController controller = new DateTimeTestController();
        assertNotNull(controller);
    }
}
