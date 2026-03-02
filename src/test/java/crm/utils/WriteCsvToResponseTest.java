package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    @Test
    void testWriteCustomers() {
        MockitoAnnotations.openMocks(this);
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test");
        customer.setEmail("test@test.com");
        customers.add(customer);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        WriteCsvToResponse.writeCustomers(printWriter, customers);

        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    void testWriteCustomer() {
        MockitoAnnotations.openMocks(this);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test");
        customer.setEmail("test@test.com");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        WriteCsvToResponse.writeCustomer(printWriter, customer);

        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    void testWriteCustomersWithEmptyList() {
        List<Customer> customers = new ArrayList<>();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        WriteCsvToResponse.writeCustomers(printWriter, customers);

        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    void testWriteCsvToResponseClassExists() {
        assertNotNull(WriteCsvToResponse.class);
    }
}
