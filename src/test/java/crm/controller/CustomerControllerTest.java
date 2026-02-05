package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        testCustomer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456)
                .build();
    }

    @Test
    public void testShowAllCustomers() {
        when(customerService.listAllCustomers()).thenReturn(new ArrayList<>());
        String view = customerController.showAllCustomers(model);
        assertEquals("customer/list", view);
        verify(model, times(1)).addAttribute(eq("customers"), any());
    }

    @Test
    public void testShowFormAddCustomer() {
        String view = customerController.showFormAddCustomer(model);
        assertEquals("customer/add", view);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    public void testProcessRequestAddCustomerWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = customerController.processRequestAddCustomer(testCustomer, bindingResult);
        assertEquals("redirect:/customer/add", view);
    }

    @Test
    public void testProcessRequestAddCustomerSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = customerController.processRequestAddCustomer(testCustomer, bindingResult);
        assertEquals("customer/success", view);
        verify(customerService, times(1)).saveCustomer(testCustomer);
    }

    @Test
    public void testShowFormEditCustomer() {
        when(customerService.showCustomer(1L)).thenReturn(testCustomer);
        String view = customerController.showFormEditCustomer(model, 1L);
        assertEquals("customer/edit", view);
        verify(model, times(1)).addAttribute("customer", testCustomer);
    }

    @Test
    public void testShowNameSearchForm() {
        String view = customerController.showNameSearchForm(model);
        assertEquals("customer/name-search", view);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    public void testProcessRequestNameSearch() {
        when(customerService.findOneByEnabledTrueAndName(anyString())).thenReturn(testCustomer);
        String view = customerController.processRequestNameSearch(testCustomer, model);
        assertEquals("customer/show-one", view);
        verify(model, times(1)).addAttribute("customer", testCustomer);
    }
}
