package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private CSVController csvController;

    private List<Customer> customers;
    private Customer customer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(1L)
                .name("Test Company")
                .email("test@example.com")
                .phone(123456789)
                .build();

        customers = new ArrayList<>();
        customers.add(customer);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testFindCustomers() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(customers);
        assertDoesNotThrow(() -> csvController.findCustomers(httpServletResponse));
        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    public void testFindCustomer() throws Exception {
        when(customerService.showCustomer(1L)).thenReturn(customer);
        assertDoesNotThrow(() -> csvController.findCustomer(1L, httpServletResponse));
        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    public void testFindCustomersCallsService() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(new ArrayList<>());
        csvController.findCustomers(httpServletResponse);
        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    public void testFindCustomerWithValidId() throws Exception {
        when(customerService.showCustomer(5L)).thenReturn(customer);
        csvController.findCustomer(5L, httpServletResponse);
        verify(customerService, times(1)).showCustomer(5L);
    }

    @Test
    public void testCSVControllerConstructor() {
        assertNotNull(csvController);
    }
}
