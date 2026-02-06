package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriteCsvToResponseTest {

    private PrintWriter printWriter;
    private StringWriter stringWriter;
    private Customer customer;
    private List<Customer> customers;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        customer = Customer.builder()
                .id(1L)
                .name("TestCompany")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("TestCity")
                .address("123 Test St")
                .enabled(1)
                .categories(new HashSet<>())
                .build();

        customers = new ArrayList<>();
        customers.add(customer);
    }

    @Test
    public void testWriteCustomersNotNull() {
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        assertNotNull(stringWriter.toString());
    }

    @Test
    public void testWriteCustomersWithValidList() {
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testWriteCustomersWithEmptyList() {
        List<Customer> emptyList = new ArrayList<>();
        WriteCsvToResponse.writeCustomers(printWriter, emptyList);
        assertNotNull(stringWriter.toString());
    }

    @Test
    public void testWriteCustomerNotNull() {
        WriteCsvToResponse.writeCustomer(printWriter, customer);
        assertNotNull(stringWriter.toString());
    }

    @Test
    public void testWriteCustomerWithValidCustomer() {
        WriteCsvToResponse.writeCustomer(printWriter, customer);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testWriteCustomersWithMultipleCustomers() {
        Customer customer2 = Customer.builder()
                .id(2L)
                .name("TestCompany2")
                .email("test2@example.com")
                .phone(987654321)
                .firstName("Jane")
                .lastName("Smith")
                .city("TestCity2")
                .address("456 Test Ave")
                .enabled(1)
                .categories(new HashSet<>())
                .build();
        customers.add(customer2);

        WriteCsvToResponse.writeCustomers(printWriter, customers);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }
}
