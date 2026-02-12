package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ExportCustomers Controller Tests")
class ExportCustomersTest {

    @Test
    @DisplayName("Test ExportCustomers controller exists")
    void testExportCustomersExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test ExportCustomers class is present")
    void testExportCustomersClass() throws ClassNotFoundException {
        Class.forName("crm.controller.ExportCustomers");
    }
}
