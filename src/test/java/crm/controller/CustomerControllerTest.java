package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
    }

    @Test
    void testShowAllCustomers() {
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(customer));

        String viewName = customerController.showAllCustomers(model);

        assertEquals("customer/list", viewName);
        verify(customerService).listAllCustomers();
        verify(model).addAttribute(eq("customers"), any());
    }

    @Test
    void testShowFormAddCustomer() {
        String viewName = customerController.showFormAddCustomer(model);

        assertEquals("customer/add", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestAddCustomerSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = customerController.processRequestAddCustomer(customer, bindingResult);

        assertEquals("customer/success", viewName);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestAddCustomerWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = customerController.processRequestAddCustomer(customer, bindingResult);

        assertEquals("redirect:/customer/add", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testShowFormEditCustomer() {
        when(customerService.showCustomer(1L)).thenReturn(customer);

        String viewName = customerController.showFormEditCustomer(model, 1L);

        assertEquals("customer/edit", viewName);
        verify(customerService).showCustomer(1L);
        verify(model).addAttribute("customer", customer);
    }

    @Test
    void testProcessRequestEditCustomerSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = customerController.processRequestEditCustomer(1L, customer, bindingResult);

        assertEquals("redirect:/customer/list", viewName);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestEditCustomerWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = customerController.processRequestEditCustomer(1L, customer, bindingResult);

        assertEquals("redirect:/customer/edit/1", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testShowNameSearchForm() {
        String viewName = customerController.showNameSearchForm(model);

        assertEquals("customer/name-search", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestNameSearch() {
        when(customerService.findOneByEnabledTrueAndName("Test Customer")).thenReturn(customer);

        String viewName = customerController.processRequestNameSearch(customer, model);

        assertEquals("customer/show-one", viewName);
        verify(customerService).findOneByEnabledTrueAndName("Test Customer");
    }
}
