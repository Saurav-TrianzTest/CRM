package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private CSVController csvController;

    @BeforeEach
    public void setUp() throws Exception {
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testFindCustomers() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();
        customers.add(customer);

        when(customerService.listAllCustomers()).thenReturn(customers);

        assertDoesNotThrow(() -> csvController.findCustomers(httpServletResponse));
        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    public void testFindCustomer() throws Exception {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        when(customerService.showCustomer(1L)).thenReturn(customer);

        assertDoesNotThrow(() -> csvController.findCustomer(1L, httpServletResponse));
        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    public void testFindCustomersWithEmptyList() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> csvController.findCustomers(httpServletResponse));
    }

    @Test
    public void testFindCustomerWithNullResult() throws Exception {
        when(customerService.showCustomer(999L)).thenReturn(null);
        assertDoesNotThrow(() -> csvController.findCustomer(999L, httpServletResponse));
    }
}
