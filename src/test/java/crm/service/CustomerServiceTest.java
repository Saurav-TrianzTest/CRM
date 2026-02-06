package crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Test
    void testInterfaceExists() {
        assertNotNull(CustomerService.class);
    }

    @Test
    void testInterfaceMethods() throws NoSuchMethodException {
        assertNotNull(CustomerService.class.getMethod("getMaxId"));
        assertNotNull(CustomerService.class.getMethod("listAllCustomers"));
        assertNotNull(CustomerService.class.getMethod("showCustomer", Long.class));
        assertNotNull(CustomerService.class.getMethod("findAllByEnabledTrue"));
        assertNotNull(CustomerService.class.getMethod("findAllByEnabledFalse"));
        assertNotNull(CustomerService.class.getMethod("saveCustomer", crm.entity.Customer.class));
    }
}
