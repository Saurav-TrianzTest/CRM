package crm.service;

import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        testCustomer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456)
                .enabled(1)
                .build();
    }

    @Test
    public void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(100L);
        Long result = customerService.getMaxId();
        assertEquals(100L, result);
    }

    @Test
    public void testListAllCustomers() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        Iterable<Customer> result = customerService.listAllCustomers();
        assertNotNull(result);
    }

    @Test
    public void testShowCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        Customer result = customerService.showCustomer(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindAllByEnabledTrue() {
        when(customerRepository.findAllByEnabled(1)).thenReturn(new ArrayList<>());
        Iterable<Customer> result = customerService.findAllByEnabledTrue();
        assertNotNull(result);
    }

    @Test
    public void testFindOneByName() {
        when(customerRepository.findOneByName("Test")).thenReturn(testCustomer);
        Customer result = customerService.findOneByName("Test");
        assertNotNull(result);
    }

    @Test
    public void testSaveCustomer() {
        customerService.saveCustomer(testCustomer);
        assertEquals(1, testCustomer.getEnabled());
        verify(customerRepository, times(1)).save(testCustomer);
    }

    @Test
    public void testFindByEmail() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(new ArrayList<>());
        Iterable<Customer> result = customerService.findByEmail("test@example.com");
        assertNotNull(result);
    }

    @Test
    public void testFindByPhone() {
        when(customerRepository.findByPhone(123456)).thenReturn(new ArrayList<>());
        Iterable<Customer> result = customerService.findByPhone(123456);
        assertNotNull(result);
    }
}
