package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerController controller;

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CustomerController(customerService);
    }

    @Test
    void testShowAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerService.listAllCustomers()).thenReturn(customers);

        String result = controller.showAllCustomers(model);

        assertEquals("customer/list", result);
        verify(model).addAttribute("customers", customers);
    }

    @Test
    void testShowFormAddCustomer() {
        String result = controller.showFormAddCustomer(model);

        assertEquals("customer/add", result);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestAddCustomerSuccess() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = controller.processRequestAddCustomer(customer, bindingResult);

        assertEquals("customer/success", result);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestAddCustomerWithErrors() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = controller.processRequestAddCustomer(customer, bindingResult);

        assertEquals("redirect:/customer/add", result);
        verify(customerService, never()).saveCustomer(customer);
    }

    @Test
    void testShowFormEditCustomer() {
        Customer customer = new Customer();
        when(customerService.showCustomer(1L)).thenReturn(customer);

        String result = controller.showFormEditCustomer(model, 1L);

        assertEquals("customer/edit", result);
        verify(model).addAttribute("customer", customer);
    }

    @Test
    void testProcessRequestEditCustomerSuccess() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = controller.processRequestEditCustomer(1L, customer, bindingResult);

        assertEquals("redirect:/customer/list", result);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testShowNameSearchForm() {
        String result = controller.showNameSearchForm(model);

        assertEquals("customer/name-search", result);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testShowEmailSearchForm() {
        String result = controller.showEmailSearchForm(model);

        assertEquals("customer/email-search", result);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testShowPhoneSearchForm() {
        String result = controller.showPhoneSearchForm(model);

        assertEquals("customer/phone-search", result);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }
}
