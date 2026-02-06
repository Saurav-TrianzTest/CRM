package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportCustomersTest {

    private ExportCustomers exportCustomers;

    @BeforeEach
    void setUp() {
        exportCustomers = new ExportCustomers();
    }

    @Test
    void testExportCustomersInstantiation() {
        assertNotNull(exportCustomers);
    }

    @Test
    void testExportCustomersClass() {
        assertTrue(exportCustomers instanceof ExportCustomers);
    }

    @Test
    void testExportCustomersNotNull() {
        ExportCustomers instance = new ExportCustomers();
        assertNotNull(instance);
    }
}
