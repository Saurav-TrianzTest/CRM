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
    void testConstructor() {
        assertNotNull(exportCustomers);
    }

    @Test
    void testExportCustomersInstantiation() {
        ExportCustomers controller = new ExportCustomers();
        assertNotNull(controller);
    }
}
