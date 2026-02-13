package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import crm.utils.WriteCsvToResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private CSVController csvController;

    private MockMvc mockMvc;

    private Customer testCustomer;
    private List<Customer> testCustomers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(csvController).build();

        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("ABC Company");
        testCustomer.setEmail("contact@abc.com");
        testCustomer.setPhone(123456789);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setCity("New York");
        testCustomer.setAddress("123 Main St");
        testCustomer.setEnabled(1);

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("XYZ Corp");
        customer2.setEmail("info@xyz.com");
        customer2.setPhone(123456789);
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setCity("Los Angeles");
        customer2.setAddress("456 Oak Ave");
        customer2.setEnabled(1);

        testCustomers = Arrays.asList(testCustomer, customer2);
    }

    @Test
    void testConstructor() {
        CSVController controller = new CSVController(customerService);
        assert controller != null;
    }

    @Test
    void testFindCustomers_Success() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(testCustomers);

        mockMvc.perform(get("/customers")
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomers_EmptyList() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/customers")
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomers_WithWriter() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(customerService.listAllCustomers()).thenReturn(testCustomers);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        csvController.findCustomers(httpServletResponse);

        verify(customerService, times(1)).listAllCustomers();
        verify(httpServletResponse, times(1)).getWriter();
    }

    @Test
    void testFindCustomers_MultipleRequests() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(testCustomers);

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/customers")
                            .accept("text/csv"))
                    .andExpect(status().isOk());
        }

        verify(customerService, times(3)).listAllCustomers();
    }

    @Test
    void testFindCustomer_Success() throws Exception {
        when(customerService.showCustomer(1L)).thenReturn(testCustomer);

        mockMvc.perform(get("/customers/{id}", 1L)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    void testFindCustomer_NullCustomer() throws Exception {
        when(customerService.showCustomer(999L)).thenReturn(null);

        mockMvc.perform(get("/customers/{id}", 999L)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(999L);
    }

    @Test
    void testFindCustomer_WithWriter() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(customerService.showCustomer(1L)).thenReturn(testCustomer);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        csvController.findCustomer(1L, httpServletResponse);

        verify(customerService, times(1)).showCustomer(1L);
        verify(httpServletResponse, times(1)).getWriter();
    }

    @Test
    void testFindCustomer_WithDifferentIds() throws Exception {
        Long[] ids = {1L, 5L, 10L, 100L, 1000L};

        for (Long id : ids) {
            Customer customer = new Customer();
            customer.setId(id);
            when(customerService.showCustomer(id)).thenReturn(customer);

            mockMvc.perform(get("/customers/{id}", id)
                            .accept("text/csv"))
                    .andExpect(status().isOk());

            verify(customerService, times(1)).showCustomer(id);
        }
    }

    @Test
    void testFindCustomer_WithZeroId() throws Exception {
        when(customerService.showCustomer(0L)).thenReturn(null);

        mockMvc.perform(get("/customers/{id}", 0L)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(0L);
    }

    @Test
    void testFindCustomer_WithNegativeId() throws Exception {
        when(customerService.showCustomer(-1L)).thenReturn(null);

        mockMvc.perform(get("/customers/{id}", -1L)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(-1L);
    }

    @Test
    void testFindCustomer_WithMaxId() throws Exception {
        when(customerService.showCustomer(Long.MAX_VALUE)).thenReturn(null);

        mockMvc.perform(get("/customers/{id}", Long.MAX_VALUE)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(Long.MAX_VALUE);
    }

    @Test
    void testFindCustomers_LargeList() throws Exception {
        Customer[] customers = new Customer[100];
        for (int i = 0; i < 100; i++) {
            customers[i] = new Customer();
            customers[i].setId((long) i);
            customers[i].setName("Company " + i);
        }
        List<Customer> largeList = Arrays.asList(customers);

        when(customerService.listAllCustomers()).thenReturn(largeList);

        mockMvc.perform(get("/customers")
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomer_CustomerWithAllFields() throws Exception {
        Customer completeCustomer = new Customer();
        completeCustomer.setId(1L);
        completeCustomer.setName("Complete Company");
        completeCustomer.setEmail("complete@company.com");
        completeCustomer.setPhone(123456789);
        completeCustomer.setFirstName("Complete");
        completeCustomer.setLastName("Customer");
        completeCustomer.setCity("Complete City");
        completeCustomer.setAddress("123 Complete St");
        completeCustomer.setEnabled(1);

        when(customerService.showCustomer(1L)).thenReturn(completeCustomer);

        mockMvc.perform(get("/customers/{id}", 1L)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    void testFindCustomer_CustomerWithMinimalFields() throws Exception {
        Customer minimalCustomer = new Customer();
        minimalCustomer.setId(1L);

        when(customerService.showCustomer(1L)).thenReturn(minimalCustomer);

        mockMvc.perform(get("/customers/{id}", 1L)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    void testFindCustomers_WithSpecialCharacters() throws Exception {
        Customer specialCustomer = new Customer();
        specialCustomer.setId(1L);
        specialCustomer.setName("Company, Inc. \"Special\"");
        specialCustomer.setEmail("test@company.com");
        specialCustomer.setAddress("123 Main St, Apt #5");

        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(specialCustomer));

        mockMvc.perform(get("/customers")
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomer_MultipleSequentialRequests() throws Exception {
        when(customerService.showCustomer(1L)).thenReturn(testCustomer);

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/customers/{id}", 1L)
                            .accept("text/csv"))
                    .andExpect(status().isOk());
        }

        verify(customerService, times(5)).showCustomer(1L);
    }

    @Test
    void testFindCustomers_SingleCustomer() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(testCustomer));

        mockMvc.perform(get("/customers")
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomer_WithContentType() throws Exception {
        when(customerService.showCustomer(1L)).thenReturn(testCustomer);

        mockMvc.perform(get("/customers/{id}", 1L)
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    void testFindCustomers_WithAcceptHeader() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(testCustomers);

        mockMvc.perform(get("/customers")
                        .header("Accept", "text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomer_DisabledCustomer() throws Exception {
        Customer disabledCustomer = new Customer();
        disabledCustomer.setId(1L);
        disabledCustomer.setName("Disabled Company");
        disabledCustomer.setEnabled(0);

        when(customerService.showCustomer(1L)).thenReturn(disabledCustomer);

        mockMvc.perform(get("/customers/{id}", 1L)
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    void testFindCustomers_MixedEnabledDisabled() throws Exception {
        Customer enabledCustomer = new Customer();
        enabledCustomer.setId(1L);
        enabledCustomer.setEnabled(1);

        Customer disabledCustomer = new Customer();
        disabledCustomer.setId(2L);
        disabledCustomer.setEnabled(0);

        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(enabledCustomer, disabledCustomer));

        mockMvc.perform(get("/customers")
                        .accept("text/csv"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listAllCustomers();
    }
}
