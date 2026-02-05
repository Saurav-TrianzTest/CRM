package crm.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    @Test
    public void testCustomerServiceInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.service.CustomerService"));
    }

    @Test
    public void testCustomerServiceHasGetMaxIdMethod() throws NoSuchMethodException {
        assertNotNull(CustomerService.class.getMethod("getMaxId"));
    }

    @Test
    public void testCustomerServiceHasListAllCustomersMethod() throws NoSuchMethodException {
        assertNotNull(CustomerService.class.getMethod("listAllCustomers"));
    }

    @Test
    public void testCustomerServiceHasShowCustomerMethod() throws NoSuchMethodException {
        assertNotNull(CustomerService.class.getMethod("showCustomer", Long.class));
    }

    @Test
    public void testCustomerServiceHasSaveCustomerMethod() throws NoSuchMethodException {
        assertNotNull(CustomerService.class.getMethod("saveCustomer", crm.entity.Customer.class));
    }

    @Test
    public void testCustomerServiceHasFindOneByNameMethod() throws NoSuchMethodException {
        assertNotNull(CustomerService.class.getMethod("findOneByName", String.class));
    }
}
