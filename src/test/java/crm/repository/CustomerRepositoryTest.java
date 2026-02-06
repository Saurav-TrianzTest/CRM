package crm.repository;

import crm.entity.Category;
import crm.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setEnabled(1);

        categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Premium");
        categories.add(category);
    }

    @Test
    void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(100L);

        Long maxId = customerRepository.getMaxId();

        assertEquals(100L, maxId);
        verify(customerRepository).getMaxId();
    }

    @Test
    void testFindAllByEnabled() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findAllByEnabled(1);

        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void testFindOneByEnabledAndName() {
        when(customerRepository.findOneByEnabledAndName(1, "Test Customer")).thenReturn(customer);

        Customer result = customerRepository.findOneByEnabledAndName(1, "Test Customer");

        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        verify(customerRepository).findOneByEnabledAndName(1, "Test Customer");
    }

    @Test
    void testFindOneByName() {
        when(customerRepository.findOneByName("Test Customer")).thenReturn(customer);

        Customer result = customerRepository.findOneByName("Test Customer");

        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        verify(customerRepository).findOneByName("Test Customer");
    }

    @Test
    void testFindByEnabledAndEmail() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndEmail(1, "test@example.com")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndEmail(1, "test@example.com");

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void testFindByEmail() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEmail("test@example.com")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEmail("test@example.com");

        assertNotNull(result);
        verify(customerRepository).findByEmail("test@example.com");
    }

    @Test
    void testFindByEnabledAndCity() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndCity(1, "New York")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndCity(1, "New York");

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(1, "New York");
    }

    @Test
    void testFindByCity() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByCity("New York")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByCity("New York");

        assertNotNull(result);
        verify(customerRepository).findByCity("New York");
    }

    @Test
    void testFindByEnabledAndCityAndAddress() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St"))
                .thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St");

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCityAndAddress(1, "New York", "123 Main St");
    }

    @Test
    void testFindByCityAndAddress() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByCityAndAddress("New York", "123 Main St");

        assertNotNull(result);
        verify(customerRepository).findByCityAndAddress("New York", "123 Main St");
    }

    @Test
    void testFindByEnabledAndPhone() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndPhone(1, 123456789)).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndPhone(1, 123456789);

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 123456789);
    }

    @Test
    void testFindByPhone() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByPhone(123456789)).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByPhone(123456789);

        assertNotNull(result);
        verify(customerRepository).findByPhone(123456789);
    }

    @Test
    void testFindByEnabledAndFirstName() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndFirstName(1, "John");

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstName(1, "John");
    }

    @Test
    void testFindByFirstName() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByFirstName("John")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByFirstName("John");

        assertNotNull(result);
        verify(customerRepository).findByFirstName("John");
    }

    @Test
    void testFindByEnabledAndLastName() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndLastName(1, "Doe")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndLastName(1, "Doe");

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndLastName(1, "Doe");
    }

    @Test
    void testFindByLastName() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByLastName("Doe")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByLastName("Doe");

        assertNotNull(result);
        verify(customerRepository).findByLastName("Doe");
    }

    @Test
    void testFindByEnabledAndFirstNameAndLastName() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe");

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstNameAndLastName(1, "John", "Doe");
    }

    @Test
    void testFindByFirstNameAndLastName() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByFirstNameAndLastName("John", "Doe");

        assertNotNull(result);
        verify(customerRepository).findByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testFindByEnabledAndCategories() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEnabledAndCategories(1, categories);

        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCategories(1, categories);
    }

    @Test
    void testFindByCategories() {
        List<Customer> customers = Arrays.asList(customer);

        when(customerRepository.findByCategories(categories)).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByCategories(categories);

        assertNotNull(result);
        verify(customerRepository).findByCategories(categories);
    }
}
