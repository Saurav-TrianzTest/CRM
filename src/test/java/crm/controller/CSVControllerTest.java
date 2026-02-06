package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CSVController csvController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindCustomers() throws IOException {
        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder().id(1L).name("Test Customer").build();
        customers.add(customer);

        when(customerService.listAllCustomers()).thenReturn(customers);

        MockHttpServletResponse response = new MockHttpServletResponse();
        csvController.findCustomers(response);

        verify(customerService, times(1)).listAllCustomers();
        assertNotNull(response.getContentAsString());
    }

    @Test
    void testFindCustomer() throws IOException {
        Customer customer = Customer.builder().id(1L).name("Test Customer").build();

        when(customerService.showCustomer(1L)).thenReturn(customer);

        MockHttpServletResponse response = new MockHttpServletResponse();
        csvController.findCustomer(1L, response);

        verify(customerService, times(1)).showCustomer(1L);
        assertNotNull(response.getContentAsString());
    }

    @Test
    void testFindCustomersWithEmptyList() throws IOException {
        List<Customer> customers = new ArrayList<>();

        when(customerService.listAllCustomers()).thenReturn(customers);

        MockHttpServletResponse response = new MockHttpServletResponse();
        csvController.findCustomers(response);

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testConstructor() {
        assertNotNull(csvController);
    }
}
