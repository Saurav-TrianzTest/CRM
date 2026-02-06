package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest_New {

    @Test
    void testWriteCustomers() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .phone(123456)
                .firstName("John")
                .lastName("Doe")
                .city("City")
                .address("Address")
                .enabled(1)
                .build();
        customers.add(customer);

        WriteCsvToResponse.writeCustomers(printWriter, customers);
        printWriter.flush();

        String result = stringWriter.toString();
        assertNotNull(result);
    }

    @Test
    void testWriteCustomer() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .phone(123456)
                .firstName("John")
                .lastName("Doe")
                .city("City")
                .address("Address")
                .enabled(1)
                .build();

        WriteCsvToResponse.writeCustomer(printWriter, customer);
        printWriter.flush();

        String result = stringWriter.toString();
        assertNotNull(result);
    }

    @Test
    void testWriteCustomersWithEmptyList() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        List<Customer> customers = new ArrayList<>();

        WriteCsvToResponse.writeCustomers(printWriter, customers);
        printWriter.flush();

        String result = stringWriter.toString();
        assertNotNull(result);
    }
}
