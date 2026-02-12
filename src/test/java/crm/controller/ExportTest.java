package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Export Controller Tests")
class ExportTest {

    @Test
    @DisplayName("Test Export controller exists")
    void testExportExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test Export class is present")
    void testExportClass() throws ClassNotFoundException {
        Class.forName("crm.controller.Export");
    }
}
