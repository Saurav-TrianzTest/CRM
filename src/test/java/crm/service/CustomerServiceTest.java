package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Test
    void testCustomerServiceInterface() {
        assertNotNull(CustomerService.class);
    }

    @Test
    void testGetMaxIdMethodExists() throws NoSuchMethodException {
        CustomerService.class.getMethod("getMaxId");
    }

    @Test
    void testListAllCustomersMethodExists() throws NoSuchMethodException {
        CustomerService.class.getMethod("listAllCustomers");
    }

    @Test
    void testShowCustomerMethodExists() throws NoSuchMethodException {
        CustomerService.class.getMethod("showCustomer", Long.class);
    }

    @Test
    void testSaveCustomerMethodExists() throws NoSuchMethodException {
        CustomerService.class.getMethod("saveCustomer", Customer.class);
    }

    @Test
    void testFindAllByEnabledTrueMethodExists() throws NoSuchMethodException {
        CustomerService.class.getMethod("findAllByEnabledTrue");
    }

    @Test
    void testFindByEmailMethodExists() throws NoSuchMethodException {
        CustomerService.class.getMethod("findByEmail", String.class);
    }

    @Test
    void testFindByPhoneMethodExists() throws NoSuchMethodException {
        CustomerService.class.getMethod("findByPhone", int.class);
    }
}
