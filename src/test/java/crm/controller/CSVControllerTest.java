package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVControllerTest {

    private CSVController csvController;
    private CustomerService customerService;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        csvController = new CSVController(customerService);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void constructor_shouldCreateCSVController() {
        // Assert
        assertNotNull(csvController);
    }

    @Test
    void findCustomers_shouldWriteCustomersToResponse() throws Exception {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .build();
        customers.add(customer);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        
        when(customerService.listAllCustomers()).thenReturn(customers);
        when(response.getWriter()).thenReturn(printWriter);
        
        // Act
        csvController.findCustomers(response);
        
        // Assert
        verify(customerService).listAllCustomers();
        verify(response).getWriter();
    }

    @Test
    void findCustomer_shouldWriteCustomerToResponse() throws Exception {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .build();
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        
        when(customerService.showCustomer(1L)).thenReturn(customer);
        when(response.getWriter()).thenReturn(printWriter);
        
        // Act
        csvController.findCustomer(1L, response);
        
        // Assert
        verify(customerService).showCustomer(1L);
        verify(response).getWriter();
    }

    @Test
    void controller_shouldHaveRestControllerAnnotation() {
        // Assert
        assertTrue(CSVController.class.isAnnotationPresent(
            org.springframework.web.bind.annotation.RestController.class));
    }
}
