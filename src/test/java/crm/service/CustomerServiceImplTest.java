package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private CustomerServiceImpl service;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(10L);
        assertEquals(10L, service.getMaxId());
        verify(customerRepository).getMaxId();
    }

    @Test
    void testListAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customers);

        Iterable<Customer> result = service.listAllCustomers();
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void testShowCustomer() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = service.showCustomer(1L);
        assertNotNull(result);
        verify(customerRepository).findById(1L);
    }

    @Test
    void testFindAllByEnabledTrue() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);

        Iterable<Customer> result = service.findAllByEnabledTrue();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAllByEnabled(0)).thenReturn(customers);

        Iterable<Customer> result = service.findAllByEnabledFalse();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName() {
        Customer customer = new Customer();
        when(customerRepository.findOneByEnabledAndName(1, "Test")).thenReturn(customer);

        Customer result = service.findOneByEnabledTrueAndName("Test");
        assertNotNull(result);
        verify(customerRepository).findOneByEnabledAndName(1, "Test");
    }

    @Test
    void testFindByEnabledTrueAndEmail() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndEmail(1, "test@test.com")).thenReturn(customers);

        Iterable<Customer> result = service.findByEnabledTrueAndEmail("test@test.com");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "test@test.com");
    }

    @Test
    void testFindByEnabledTrueAndPhone() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndPhone(1, 123456)).thenReturn(customers);

        Iterable<Customer> result = service.findByEnabledTrueAndPhone(123456);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 123456);
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenReturn(customer);

        service.saveCustomer(customer);

        assertEquals(1, customer.getEnabled());
        verify(customerRepository).save(customer);
    }

    @Test
    void testFindByEnabledTrueAndCity() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndCity(1, "NYC")).thenReturn(customers);

        Iterable<Customer> result = service.findByEnabledTrueAndCity("NYC");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(1, "NYC");
    }
}
