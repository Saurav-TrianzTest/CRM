package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WriteCsvToResponseTest {

    @Test
    void testClassExists() {
        assertNotNull(WriteCsvToResponse.class);
    }

    @Test
    void testClassInstantiation() {
        WriteCsvToResponse utils = new WriteCsvToResponse();
        assertNotNull(utils);
    }

    @Test
    void testWriteCustomersMethodExists() throws NoSuchMethodException {
        assertNotNull(WriteCsvToResponse.class.getMethod("writeCustomers",
                PrintWriter.class, List.class));
    }

    @Test
    void testWriteCustomerMethodExists() throws NoSuchMethodException {
        assertNotNull(WriteCsvToResponse.class.getMethod("writeCustomer",
                PrintWriter.class, Customer.class));
    }

    @Test
    void testWriteCustomersWithEmptyList() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        List<Customer> customers = Arrays.asList();

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCustomersWithValidCustomer() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test");
        customer.setEmail("test@example.com");

        List<Customer> customers = Arrays.asList(customer);

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCustomerWithValidCustomer() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test");
        customer.setEmail("test@example.com");

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomer(printWriter, customer);
        });
    }
}
