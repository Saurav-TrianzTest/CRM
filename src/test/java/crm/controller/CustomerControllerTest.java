package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

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
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(1L)
                .name("Test Company")
                .email("test@example.com")
                .phone(123456789)
                .build();
    }

    @Test
    public void testShowAllCustomers() {
        when(customerService.listAllCustomers()).thenReturn(new ArrayList<>());
        String result = customerController.showAllCustomers(model);
        assertEquals("customer/list", result);
        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    public void testShowFormAddCustomer() {
        String result = customerController.showFormAddCustomer(model);
        assertEquals("customer/add", result);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    public void testProcessRequestAddCustomerWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = customerController.processRequestAddCustomer(customer, bindingResult);
        assertEquals("redirect:/customer/add", result);
    }

    @Test
    public void testProcessRequestAddCustomerSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = customerController.processRequestAddCustomer(customer, bindingResult);
        assertEquals("customer/success", result);
        verify(customerService, times(1)).saveCustomer(customer);
    }

    @Test
    public void testShowFormEditCustomer() {
        when(customerService.showCustomer(1L)).thenReturn(customer);
        String result = customerController.showFormEditCustomer(model, 1L);
        assertEquals("customer/edit", result);
        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    public void testProcessRequestEditCustomerWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        assertEquals("redirect:/customer/edit/1", result);
    }

    @Test
    public void testProcessRequestEditCustomerSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        assertEquals("redirect:/customer/list", result);
        verify(customerService, times(1)).saveCustomer(customer);
    }

    @Test
    public void testShowNameSearchForm() {
        String result = customerController.showNameSearchForm(model);
        assertEquals("customer/name-search", result);
    }

    @Test
    public void testProcessRequestNameSearch() {
        when(customerService.findOneByEnabledTrueAndName("Test Company")).thenReturn(customer);
        String result = customerController.processRequestNameSearch(customer, model);
        assertEquals("customer/show-one", result);
    }

    @Test
    public void testShowEmailSearchForm() {
        String result = customerController.showEmailSearchForm(model);
        assertEquals("customer/email-search", result);
    }

    @Test
    public void testProcessRequestEmailSearch() {
        when(customerService.findByEnabledTrueAndEmail("test@example.com")).thenReturn(new ArrayList<>());
        String result = customerController.processRequestEmailSearch(customer, model);
        assertEquals("customer/show-list", result);
    }

    @Test
    public void testShowPhoneSearchForm() {
        String result = customerController.showPhoneSearchForm(model);
        assertEquals("customer/phone-search", result);
    }

    @Test
    public void testCustomerControllerConstructor() {
        assertNotNull(customerController);
    }
}
