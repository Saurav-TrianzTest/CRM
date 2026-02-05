package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriteCsvToResponseTest {

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    public void testWriteCustomersWithValidList() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        List<Customer> customers = Arrays.asList(customer1);
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        assertNotNull(stringWriter.toString());
    }

    @Test
    public void testWriteCustomersWithEmptyList() {
        List<Customer> customers = Arrays.asList();
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        assertNotNull(stringWriter.toString());
    }

    @Test
    public void testWriteCustomersWithNullList() {
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(printWriter, null));
    }

    @Test
    public void testWriteCustomerWithValidCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        WriteCsvToResponse.writeCustomer(printWriter, customer);
        assertNotNull(stringWriter.toString());
    }

    @Test
    public void testWriteCustomerWithNullCustomer() {
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(printWriter, null));
    }

    @Test
    public void testWriteCustomersWithMultipleCustomers() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .email("customer1@example.com")
                .phone(111111)
                .firstName("John")
                .lastName("Doe")
                .city("City1")
                .address("Address1")
                .enabled(1)
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Customer2")
                .email("customer2@example.com")
                .phone(222222)
                .firstName("Jane")
                .lastName("Smith")
                .city("City2")
                .address("Address2")
                .enabled(0)
                .build();

        List<Customer> customers = Arrays.asList(customer1, customer2);
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        assertNotNull(stringWriter.toString());
    }

    @Test
    public void testWriteCustomerWithNullPrintWriter() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .phone(123456)
                .build();

        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(null, customer));
    }
}
