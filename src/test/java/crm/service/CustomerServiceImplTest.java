package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerServiceImpl Tests")
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");

        categories = new HashSet<>();
        categories.add(category);

        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .enabled(1)
                .categories(categories)
                .build();
    }

    @Test
    @DisplayName("Test getMaxId returns maximum customer id")
    void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(10L);

        Long maxId = customerService.getMaxId();

        assertEquals(10L, maxId);
        verify(customerRepository, times(1)).getMaxId();
    }

    @Test
    @DisplayName("Test listAllCustomers returns all customers")
    void testListAllCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        Iterable<Customer> result = customerService.listAllCustomers();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test showCustomer returns customer by id")
    void testShowCustomer() {
        when(customerRepository.findById(1L).orElse(null)).thenReturn(customer);

        Customer found = customerService.showCustomer(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(customerRepository, times(1)).findById(1L).orElse(null);
    }

    @Test
    @DisplayName("Test findAllByEnabledTrue returns only enabled customers")
    void testFindAllByEnabledTrue() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);

        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    @DisplayName("Test findAllByEnabledFalse returns only disabled customers")
    void testFindAllByEnabledFalse() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAllByEnabled(0)).thenReturn(customers);

        Iterable<Customer> result = customerService.findAllByEnabledFalse();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(0);
    }

    @Test
    @DisplayName("Test findOneByEnabledTrueAndName returns customer")
    void testFindOneByEnabledTrueAndName() {
        when(customerRepository.findOneByEnabledAndName(1, "Test Customer")).thenReturn(customer);

        Customer found = customerService.findOneByEnabledTrueAndName("Test Customer");

        assertNotNull(found);
        assertEquals("Test Customer", found.getName());
        verify(customerRepository, times(1)).findOneByEnabledAndName(1, "Test Customer");
    }

    @Test
    @DisplayName("Test findOneByName returns customer regardless of status")
    void testFindOneByName() {
        when(customerRepository.findOneByName("Test Customer")).thenReturn(customer);

        Customer found = customerService.findOneByName("Test Customer");

        assertNotNull(found);
        verify(customerRepository, times(1)).findOneByName("Test Customer");
    }

    @Test
    @DisplayName("Test findByEnabledTrueAndEmail returns customers by email")
    void testFindByEnabledTrueAndEmail() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    @DisplayName("Test findByPhone returns customers by phone")
    void testFindByPhone() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByPhone(123456789)).thenReturn(customers);

        Iterable<Customer> result = customerService.findByPhone(123456789);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByPhone(123456789);
    }

    @Test
    @DisplayName("Test findByCategories returns customers by category")
    void testFindByCategories() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCategories(categories)).thenReturn(customers);

        Iterable<Customer> result = customerService.findByCategories(categories);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByCategories(categories);
    }

    @Test
    @DisplayName("Test findByFirstName returns customers by first name")
    void testFindByFirstName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstName("John")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByFirstName("John");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByFirstName("John");
    }

    @Test
    @DisplayName("Test findByLastName returns customers by last name")
    void testFindByLastName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByLastName("Doe")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByLastName("Doe");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByLastName("Doe");
    }

    @Test
    @DisplayName("Test findByFirstNameAndLastName returns customers by full name")
    void testFindByFirstNameAndLastName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByFirstNameAndLastName("John", "Doe");
    }

    @Test
    @DisplayName("Test findByCity returns customers by city")
    void testFindByCity() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCity("New York")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByCity("New York");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByCity("New York");
    }

    @Test
    @DisplayName("Test findByCityAndAddress returns customers by city and address")
    void testFindByCityAndAddress() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByCityAndAddress("New York", "123 Main St");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByCityAndAddress("New York", "123 Main St");
    }

    @Test
    @DisplayName("Test saveCustomer sets enabled to 1")
    void testSaveCustomer() {
        Customer newCustomer = Customer.builder()
                .name("New Customer")
                .email("new@example.com")
                .enabled(0)
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        customerService.saveCustomer(newCustomer);

        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    @DisplayName("Test findByEnabledTrueAndFirstName returns enabled customers")
    void testFindByEnabledTrueAndFirstName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndFirstName(1, "John");
    }

    @Test
    @DisplayName("Test findByEnabledFalseAndEmail returns disabled customers by email")
    void testFindByEnabledFalseAndEmail() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(0, "test@example.com")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail("test@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndEmail(0, "test@example.com");
    }

    @Test
    @DisplayName("Test findByEnabledTrueAndCategories returns enabled customers by category")
    void testFindByEnabledTrueAndCategories() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(customers);

        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCategories(1, categories);
    }
}
