package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVControllerTest {

    private CSVController controller;

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CSVController(customerService);
    }

    @Test
    void testFindCustomers() throws IOException {
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(1L);
        customers.add(customer);

        when(customerService.listAllCustomers()).thenReturn(customers);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        controller.findCustomers(httpServletResponse);

        verify(customerService).listAllCustomers();
        verify(httpServletResponse).getWriter();
    }

    @Test
    void testFindCustomer() throws IOException {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerService.showCustomer(1L)).thenReturn(customer);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        controller.findCustomer(1L, httpServletResponse);

        verify(customerService).showCustomer(1L);
        verify(httpServletResponse).getWriter();
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }
}
