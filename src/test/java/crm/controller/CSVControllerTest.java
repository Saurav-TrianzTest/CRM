package crm.controller;

import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CSVController controller;

    @Test
    void testControllerClassExists() {
        assertNotNull(CSVController.class);
    }

    @Test
    void testControllerInstantiation() {
        assertNotNull(controller);
    }
}
