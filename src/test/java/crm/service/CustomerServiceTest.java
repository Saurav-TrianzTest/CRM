package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Test
    void customerService_shouldBeInterface() {
        // Assert
        assertTrue(CustomerService.class.isInterface());
    }

    @Test
    void getMaxId_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("getMaxId");
        });
    }

    @Test
    void listAllCustomers_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("listAllCustomers");
        });
    }

    @Test
    void showCustomer_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("showCustomer", Long.class);
        });
    }

    @Test
    void findAllByEnabledTrue_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("findAllByEnabledTrue");
        });
    }

    @Test
    void findAllByEnabledFalse_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("findAllByEnabledFalse");
        });
    }

    @Test
    void findOneByEnabledTrueAndName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("findOneByEnabledTrueAndName", String.class);
        });
    }

    @Test
    void findByEnabledTrueAndEmail_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("findByEnabledTrueAndEmail", String.class);
        });
    }

    @Test
    void findByEnabledTrueAndPhone_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("findByEnabledTrueAndPhone", int.class);
        });
    }

    @Test
    void findByEnabledTrueAndCategories_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("findByEnabledTrueAndCategories", Set.class);
        });
    }

    @Test
    void saveCustomer_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerService.class.getMethod("saveCustomer", Customer.class);
        });
    }

    @Test
    void showCustomer_shouldReturnCustomer() throws NoSuchMethodException {
        // Arrange
        var method = CustomerService.class.getMethod("showCustomer", Long.class);
        
        // Assert
        assertEquals(Customer.class, method.getReturnType());
    }
}
