package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ExportCustomersTest {

    @Test
    void testControllerClassExists() {
        assertNotNull(ExportCustomers.class);
    }

    @Test
    void testControllerInstantiation() {
        ExportCustomers controller = new ExportCustomers();
        assertNotNull(controller);
    }
}
