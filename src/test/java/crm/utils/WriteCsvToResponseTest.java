package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    private Customer customer;
    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
        
        customers = new ArrayList<>();
        customers.add(customer);
    }

    @Test
    void writeCustomers_withValidList_shouldWriteCsv() {
        // Act
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        printWriter.flush();
        
        // Assert
        String result = stringWriter.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void writeCustomers_withEmptyList_shouldHandleGracefully() {
        // Arrange
        List<Customer> emptyList = new ArrayList<>();
        
        // Act
        WriteCsvToResponse.writeCustomers(printWriter, emptyList);
        printWriter.flush();
        
        // Assert
        assertNotNull(stringWriter.toString());
    }

    @Test
    void writeCustomer_withValidCustomer_shouldWriteCsv() {
        // Act
        WriteCsvToResponse.writeCustomer(printWriter, customer);
        printWriter.flush();
        
        // Assert
        String result = stringWriter.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void writeCustomer_withNullPrintWriter_shouldHandleException() {
        // Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(null, customer));
    }

    @Test
    void writeCustomers_withNullPrintWriter_shouldHandleException() {
        // Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(null, customers));
    }
}
