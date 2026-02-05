package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ExportCustomersTest {

    private ExportCustomers exportCustomers;

    @BeforeEach
    public void setUp() {
        exportCustomers = new ExportCustomers();
    }

    @Test
    public void testExportCustomersCreation() {
        assertNotNull(exportCustomers);
    }

    @Test
    public void testExportCustomersInstantiation() {
        ExportCustomers controller = new ExportCustomers();
        assertNotNull(controller);
    }

    @Test
    public void testExportCustomersClassExists() {
        assertDoesNotThrow(() -> {
            ExportCustomers controller = new ExportCustomers();
        });
    }
}
