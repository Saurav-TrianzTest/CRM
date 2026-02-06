package crm.utils;

import com.opencsv.exceptions.CsvException;
import crm.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    private Customer testCustomer;
    private List<Customer> testCustomers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        testCustomer = Customer.builder()
                .id(1L)
                .name("TestCompany")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("TestCity")
                .address("123 Test St")
                .enabled(1)
                .build();

        testCustomers = new ArrayList<>();
        testCustomers.add(testCustomer);

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
                .build();
        testCustomers.add(customer2);
    }

    @Test
    void testWriteCustomersWithValidList() {
        WriteCsvToResponse.writeCustomers(printWriter, testCustomers);
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
        assertTrue(output.length() > 0);
    }

    @Test
    void testWriteCustomersWithEmptyList() {
        List<Customer> emptyList = new ArrayList<>();
        WriteCsvToResponse.writeCustomers(printWriter, emptyList);
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    void testWriteCustomersWithNullValues() {
        Customer customerWithNulls = Customer.builder()
                .id(3L)
                .name("NullTest")
                .email("null@example.com")
                .phone(0)
                .firstName(null)
                .lastName(null)
                .city(null)
                .address(null)
                .enabled(0)
                .build();

        List<Customer> customersWithNulls = new ArrayList<>();
        customersWithNulls.add(customerWithNulls);

        WriteCsvToResponse.writeCustomers(printWriter, customersWithNulls);
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    void testWriteCustomerWithValidCustomer() {
        WriteCsvToResponse.writeCustomer(printWriter, testCustomer);
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
        assertTrue(output.length() > 0);
    }

    @Test
    void testWriteCustomerWithNullValues() {
        Customer customerWithNulls = Customer.builder()
                .id(1L)
                .name("NullTest")
                .email("null@example.com")
                .phone(0)
                .firstName(null)
                .lastName(null)
                .city(null)
                .address(null)
                .enabled(0)
                .build();

        WriteCsvToResponse.writeCustomer(printWriter, customerWithNulls);
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    void testWriteCustomerMultipleTimes() {
        WriteCsvToResponse.writeCustomer(printWriter, testCustomer);

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
                .build();

        WriteCsvToResponse.writeCustomer(printWriter, customer2);
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
        assertTrue(output.length() > 0);
    }

    @Test
    void testWriteCustomersWithSingleCustomer() {
        List<Customer> singleCustomerList = new ArrayList<>();
        singleCustomerList.add(testCustomer);

        WriteCsvToResponse.writeCustomers(printWriter, singleCustomerList);
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
        assertTrue(output.length() > 0);
    }
}
