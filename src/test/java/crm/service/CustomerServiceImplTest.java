package crm.service;

import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void constructor_shouldCreateCustomerServiceImpl() {
        // Assert
        assertNotNull(customerService);
    }

    @Test
    void getMaxId_shouldReturnMaxId() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(10L);
        
        // Act
        Long result = customerService.getMaxId();
        
        // Assert
        assertEquals(10L, result);
        verify(customerRepository).getMaxId();
    }

    @Test
    void listAllCustomers_shouldReturnAllCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customers);
        
        // Act
        Iterable<Customer> result = customerService.listAllCustomers();
        
        // Assert
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void showCustomer_shouldReturnCustomer() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .build();
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        
        // Act
        Customer result = customerService.showCustomer(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository).findById(1L);
    }

    @Test
    void showCustomer_withNonExistentId_shouldReturnNull() {
        // Arrange
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act
        Customer result = customerService.showCustomer(999L);
        
        // Assert
        assertNull(result);
        verify(customerRepository).findById(999L);
    }

    @Test
    void findAllByEnabledTrue_shouldReturnEnabledCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);
        
        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();
        
        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void findAllByEnabledFalse_shouldReturnDisabledCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAllByEnabled(0)).thenReturn(customers);
        
        // Act
        Iterable<Customer> result = customerService.findAllByEnabledFalse();
        
        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void saveCustomer_shouldSaveCustomerWithEnabledFlag() {
        // Arrange
        Customer customer = Customer.builder()
                .name("Test Customer")
                .build();
        
        when(customerRepository.save(customer)).thenReturn(customer);
        
        // Act
        customerService.saveCustomer(customer);
        
        // Assert
        assertEquals(1, customer.getEnabled());
        verify(customerRepository).save(customer);
    }

    @Test
    void findOneByEnabledTrueAndName_shouldReturnCustomer() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .build();
        
        when(customerRepository.findOneByEnabledAndName(1, "Test")).thenReturn(customer);
        
        // Act
        Customer result = customerService.findOneByEnabledTrueAndName("Test");
        
        // Assert
        assertNotNull(result);
        assertEquals("Test", result.getName());
        verify(customerRepository).findOneByEnabledAndName(1, "Test");
    }

    @Test
    void service_shouldHaveServiceAnnotation() {
        // Assert
        assertTrue(CustomerServiceImpl.class.isAnnotationPresent(
            org.springframework.stereotype.Service.class));
    }
}
