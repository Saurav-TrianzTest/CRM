package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for CustomerServiceImpl
 * Tests all public methods, constructors, edge cases, null checks, and boundary conditions
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private Category testCategory;
    private Set<Category> testCategories;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("VIP");

        testCategories = new HashSet<>();
        testCategories.add(testCategory);

        testCustomer = Customer.builder()
                .id(1L)
                .name("TestCompany")
                .email("test@company.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .categories(testCategories)
                .build();
    }

    // Constructor Tests
    @Test
    void testConstructor_WithValidRepository() {
        CustomerRepository repository = mock(CustomerRepository.class);
        CustomerServiceImpl service = new CustomerServiceImpl(repository);
        assertNotNull(service);
    }

    @Test
    void testConstructor_WithNullRepository() {
        CustomerServiceImpl service = new CustomerServiceImpl(null);
        assertNotNull(service);
    }

    // getMaxId Tests
    @Test
    void testGetMaxId_WithExistingCustomers() {
        // Arrange
        Long maxId = 100L;
        when(customerRepository.getMaxId()).thenReturn(maxId);

        // Act
        Long result = customerService.getMaxId();

        // Assert
        assertNotNull(result);
        assertEquals(maxId, result);
        verify(customerRepository, times(1)).getMaxId();
    }

    @Test
    void testGetMaxId_WithNoCustomers() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(null);

        // Act
        Long result = customerService.getMaxId();

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).getMaxId();
    }

    @Test
    void testGetMaxId_WithZeroId() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(0L);

        // Act
        Long result = customerService.getMaxId();

        // Assert
        assertEquals(0L, result);
        verify(customerRepository, times(1)).getMaxId();
    }

    // listAllCustomers Tests
    @Test
    void testListAllCustomers_WithMultipleCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer,
                Customer.builder().id(2L).name("Customer2").email("test2@company.com").build());
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.listAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testListAllCustomers_WithEmptyList() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.listAllCustomers();

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(customerRepository, times(1)).findAll();
    }

    // showCustomer Tests
    @Test
    void testShowCustomer_WithExistingCustomer() {
        // Arrange
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));

        // Act
        Customer result = customerService.showCustomer(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testShowCustomer_WithNonExistingCustomer() {
        // Arrange
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act
        Customer result = customerService.showCustomer(customerId);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testShowCustomer_WithNullId() {
        // Arrange
        when(customerRepository.findById(null)).thenReturn(Optional.empty());

        // Act
        Customer result = customerService.showCustomer(null);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findById(null);
    }

    // findAllByEnabledTrue Tests
    @Test
    void testFindAllByEnabledTrue_WithEnabledCustomers() {
        // Arrange
        List<Customer> enabledCustomers = Arrays.asList(testCustomer);
        when(customerRepository.findAllByEnabled(1)).thenReturn(enabledCustomers);

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledTrue_WithNoEnabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(1)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    // findAllByEnabledFalse Tests
    @Test
    void testFindAllByEnabledFalse_WithDisabledCustomers() {
        // Arrange
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .name("DisabledCustomer")
                .email("disabled@company.com")
                .enabled(0)
                .build();
        List<Customer> disabledCustomers = Arrays.asList(disabledCustomer);
        when(customerRepository.findAllByEnabled(0)).thenReturn(disabledCustomers);

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledFalse();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findAllByEnabled(0);
    }

    @Test
    void testFindAllByEnabledFalse_WithNoDisabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(0)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledFalse();

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(customerRepository, times(1)).findAllByEnabled(0);
    }

    // findOneByEnabledTrueAndName Tests
    @Test
    void testFindOneByEnabledTrueAndName_WithExistingCustomer() {
        // Arrange
        String name = "TestCompany";
        when(customerRepository.findOneByEnabledAndName(1, name)).thenReturn(testCustomer);

        // Act
        Customer result = customerService.findOneByEnabledTrueAndName(name);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(customerRepository, times(1)).findOneByEnabledAndName(1, name);
    }

    @Test
    void testFindOneByEnabledTrueAndName_WithNonExistingCustomer() {
        // Arrange
        String name = "NonExisting";
        when(customerRepository.findOneByEnabledAndName(1, name)).thenReturn(null);

        // Act
        Customer result = customerService.findOneByEnabledTrueAndName(name);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findOneByEnabledAndName(1, name);
    }

    @Test
    void testFindOneByEnabledTrueAndName_WithNullName() {
        // Arrange
        when(customerRepository.findOneByEnabledAndName(1, null)).thenReturn(null);

        // Act
        Customer result = customerService.findOneByEnabledTrueAndName(null);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findOneByEnabledAndName(1, null);
    }

    // findOneByEnabledFalseAndName Tests
    @Test
    void testFindOneByEnabledFalseAndName_WithExistingDisabledCustomer() {
        // Arrange
        String name = "DisabledCompany";
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .name(name)
                .email("disabled@company.com")
                .enabled(0)
                .build();
        when(customerRepository.findOneByEnabledAndName(0, name)).thenReturn(disabledCustomer);

        // Act
        Customer result = customerService.findOneByEnabledFalseAndName(name);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(0, result.getEnabled());
        verify(customerRepository, times(1)).findOneByEnabledAndName(0, name);
    }

    @Test
    void testFindOneByEnabledFalseAndName_WithNonExistingCustomer() {
        // Arrange
        String name = "NonExisting";
        when(customerRepository.findOneByEnabledAndName(0, name)).thenReturn(null);

        // Act
        Customer result = customerService.findOneByEnabledFalseAndName(name);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findOneByEnabledAndName(0, name);
    }

    // findOneByName Tests
    @Test
    void testFindOneByName_WithExistingCustomer() {
        // Arrange
        String name = "TestCompany";
        when(customerRepository.findOneByName(name)).thenReturn(testCustomer);

        // Act
        Customer result = customerService.findOneByName(name);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(customerRepository, times(1)).findOneByName(name);
    }

    @Test
    void testFindOneByName_WithNullName() {
        // Arrange
        when(customerRepository.findOneByName(null)).thenReturn(null);

        // Act
        Customer result = customerService.findOneByName(null);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findOneByName(null);
    }

    // findByEnabledTrueAndEmail Tests
    @Test
    void testFindByEnabledTrueAndEmail_WithExistingCustomers() {
        // Arrange
        String email = "test@company.com";
        when(customerRepository.findByEnabledAndEmail(1, email)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndEmail(1, email);
    }

    @Test
    void testFindByEnabledTrueAndEmail_WithNullEmail() {
        // Arrange
        when(customerRepository.findByEnabledAndEmail(1, null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail(null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndEmail(1, null);
    }

    // findByEnabledFalseAndEmail Tests
    @Test
    void testFindByEnabledFalseAndEmail_WithExistingCustomers() {
        // Arrange
        String email = "disabled@company.com";
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .email(email)
                .enabled(0)
                .build();
        when(customerRepository.findByEnabledAndEmail(0, email)).thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndEmail(0, email);
    }

    // findByEmail Tests
    @Test
    void testFindByEmail_WithExistingCustomers() {
        // Arrange
        String email = "test@company.com";
        when(customerRepository.findByEmail(email)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_WithNonExistingEmail() {
        // Arrange
        String email = "nonexisting@company.com";
        when(customerRepository.findByEmail(email)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEmail(email);

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(customerRepository, times(1)).findByEmail(email);
    }

    // findByEnabledTrueAndPhone Tests
    @Test
    void testFindByEnabledTrueAndPhone_WithExistingCustomers() {
        // Arrange
        int phone = 123456789;
        when(customerRepository.findByEnabledAndPhone(1, phone)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(phone);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndPhone(1, phone);
    }

    @Test
    void testFindByEnabledTrueAndPhone_WithZeroPhone() {
        // Arrange
        int phone = 0;
        when(customerRepository.findByEnabledAndPhone(1, phone)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(phone);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndPhone(1, phone);
    }

    @Test
    void testFindByEnabledTrueAndPhone_WithNegativePhone() {
        // Arrange
        int phone = -123456789;
        when(customerRepository.findByEnabledAndPhone(1, phone)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(phone);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndPhone(1, phone);
    }

    // findByEnabledFalseAndPhone Tests
    @Test
    void testFindByEnabledFalseAndPhone_WithExistingCustomers() {
        // Arrange
        int phone = 987654321;
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .phone(phone)
                .enabled(0)
                .build();
        when(customerRepository.findByEnabledAndPhone(0, phone)).thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndPhone(phone);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndPhone(0, phone);
    }

    // findByPhone Tests
    @Test
    void testFindByPhone_WithExistingCustomers() {
        // Arrange
        int phone = 123456789;
        when(customerRepository.findByPhone(phone)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByPhone(phone);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByPhone(phone);
    }

    @Test
    void testFindByPhone_WithNonExistingPhone() {
        // Arrange
        int phone = 999999999;
        when(customerRepository.findByPhone(phone)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByPhone(phone);

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(customerRepository, times(1)).findByPhone(phone);
    }

    // findByEnabledTrueAndCategories Tests
    @Test
    void testFindByEnabledTrueAndCategories_WithExistingCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndCategories(1, testCategories)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(testCategories);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndCategories(1, testCategories);
    }

    @Test
    void testFindByEnabledTrueAndCategories_WithEmptyCategories() {
        // Arrange
        Set<Category> emptyCategories = new HashSet<>();
        when(customerRepository.findByEnabledAndCategories(1, emptyCategories)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(emptyCategories);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCategories(1, emptyCategories);
    }

    @Test
    void testFindByEnabledTrueAndCategories_WithNullCategories() {
        // Arrange
        when(customerRepository.findByEnabledAndCategories(1, null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCategories(1, null);
    }

    // findByEnabledFalseAndCategories Tests
    @Test
    void testFindByEnabledFalseAndCategories_WithExistingCustomers() {
        // Arrange
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .enabled(0)
                .categories(testCategories)
                .build();
        when(customerRepository.findByEnabledAndCategories(0, testCategories)).thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndCategories(testCategories);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndCategories(0, testCategories);
    }

    // findByCategories Tests
    @Test
    void testFindByCategories_WithExistingCustomers() {
        // Arrange
        when(customerRepository.findByCategories(testCategories)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByCategories(testCategories);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByCategories(testCategories);
    }

    @Test
    void testFindByCategories_WithNullCategories() {
        // Arrange
        when(customerRepository.findByCategories(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByCategories(null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByCategories(null);
    }

    // findByEnabledTrueAndFirstName Tests
    @Test
    void testFindByEnabledTrueAndFirstName_WithExistingCustomers() {
        // Arrange
        String firstName = "John";
        when(customerRepository.findByEnabledAndFirstName(1, firstName)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName(firstName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndFirstName(1, firstName);
    }

    @Test
    void testFindByEnabledTrueAndFirstName_WithNullFirstName() {
        // Arrange
        when(customerRepository.findByEnabledAndFirstName(1, null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName(null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndFirstName(1, null);
    }

    // findByEnabledFalseAndFirstName Tests
    @Test
    void testFindByEnabledFalseAndFirstName_WithExistingCustomers() {
        // Arrange
        String firstName = "Jane";
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .firstName(firstName)
                .enabled(0)
                .build();
        when(customerRepository.findByEnabledAndFirstName(0, firstName)).thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstName(firstName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndFirstName(0, firstName);
    }

    // findByFirstName Tests
    @Test
    void testFindByFirstName_WithExistingCustomers() {
        // Arrange
        String firstName = "John";
        when(customerRepository.findByFirstName(firstName)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByFirstName(firstName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByFirstName(firstName);
    }

    @Test
    void testFindByFirstName_WithEmptyString() {
        // Arrange
        String firstName = "";
        when(customerRepository.findByFirstName(firstName)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByFirstName(firstName);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByFirstName(firstName);
    }

    // findByEnabledTrueAndLastName Tests
    @Test
    void testFindByEnabledTrueAndLastName_WithExistingCustomers() {
        // Arrange
        String lastName = "Doe";
        when(customerRepository.findByEnabledAndLastName(1, lastName)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName(lastName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndLastName(1, lastName);
    }

    @Test
    void testFindByEnabledTrueAndLastName_WithNullLastName() {
        // Arrange
        when(customerRepository.findByEnabledAndLastName(1, null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName(null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndLastName(1, null);
    }

    // findByEnabledFalseAndLastName Tests
    @Test
    void testFindByEnabledFalseAndLastName_WithExistingCustomers() {
        // Arrange
        String lastName = "Smith";
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .lastName(lastName)
                .enabled(0)
                .build();
        when(customerRepository.findByEnabledAndLastName(0, lastName)).thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndLastName(lastName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndLastName(0, lastName);
    }

    // findByLastName Tests
    @Test
    void testFindByLastName_WithExistingCustomers() {
        // Arrange
        String lastName = "Doe";
        when(customerRepository.findByLastName(lastName)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByLastName(lastName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByLastName(lastName);
    }

    @Test
    void testFindByLastName_WithEmptyString() {
        // Arrange
        String lastName = "";
        when(customerRepository.findByLastName(lastName)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByLastName(lastName);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByLastName(lastName);
    }

    // findByEnabledTrueAndFirstNameAndLastName Tests
    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName_WithExistingCustomers() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, firstName, lastName))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndFirstNameAndLastName(1, firstName, lastName);
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName_WithNullParameters() {
        // Arrange
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, null, null))
                .thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName(null, null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndFirstNameAndLastName(1, null, null);
    }

    // findByEnabledFalseAndFirstNameAndLastName Tests
    @Test
    void testFindByEnabledFalseAndFirstNameAndLastName_WithExistingCustomers() {
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .firstName(firstName)
                .lastName(lastName)
                .enabled(0)
                .build();
        when(customerRepository.findByEnabledAndFirstNameAndLastName(0, firstName, lastName))
                .thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstNameAndLastName(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndFirstNameAndLastName(0, firstName, lastName);
    }

    // findByFirstNameAndLastName Tests
    @Test
    void testFindByFirstNameAndLastName_WithExistingCustomers() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        when(customerRepository.findByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByFirstNameAndLastName(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    void testFindByFirstNameAndLastName_WithNonExistingCustomers() {
        // Arrange
        String firstName = "NonExisting";
        String lastName = "Customer";
        when(customerRepository.findByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByFirstNameAndLastName(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(customerRepository, times(1)).findByFirstNameAndLastName(firstName, lastName);
    }

    // findByEnabledTrueAndCity Tests
    @Test
    void testFindByEnabledTrueAndCity_WithExistingCustomers() {
        // Arrange
        String city = "New York";
        when(customerRepository.findByEnabledAndCity(1, city)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity(city);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndCity(1, city);
    }

    @Test
    void testFindByEnabledTrueAndCity_WithNullCity() {
        // Arrange
        when(customerRepository.findByEnabledAndCity(1, null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity(null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCity(1, null);
    }

    // findByEnabledFalseAndCity Tests
    @Test
    void testFindByEnabledFalseAndCity_WithExistingCustomers() {
        // Arrange
        String city = "Los Angeles";
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .city(city)
                .enabled(0)
                .build();
        when(customerRepository.findByEnabledAndCity(0, city)).thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndCity(city);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndCity(0, city);
    }

    // findByCity Tests
    @Test
    void testFindByCity_WithExistingCustomers() {
        // Arrange
        String city = "New York";
        when(customerRepository.findByCity(city)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByCity(city);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByCity(city);
    }

    @Test
    void testFindByCity_WithEmptyString() {
        // Arrange
        String city = "";
        when(customerRepository.findByCity(city)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByCity(city);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByCity(city);
    }

    // findByEnabledTrueAndCityAndAddress Tests
    @Test
    void testFindByEnabledTrueAndCityAndAddress_WithExistingCustomers() {
        // Arrange
        String city = "New York";
        String address = "123 Main St";
        when(customerRepository.findByEnabledAndCityAndAddress(1, city, address))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress(city, address);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndCityAndAddress(1, city, address);
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress_WithNullParameters() {
        // Arrange
        when(customerRepository.findByEnabledAndCityAndAddress(1, null, null))
                .thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress(null, null);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCityAndAddress(1, null, null);
    }

    // findByEnabledFalseAndCityAndAddress Tests
    @Test
    void testFindByEnabledFalseAndCityAndAddress_WithExistingCustomers() {
        // Arrange
        String city = "Chicago";
        String address = "456 Oak Ave";
        Customer disabledCustomer = Customer.builder()
                .id(2L)
                .city(city)
                .address(address)
                .enabled(0)
                .build();
        when(customerRepository.findByEnabledAndCityAndAddress(0, city, address))
                .thenReturn(Arrays.asList(disabledCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndCityAndAddress(city, address);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByEnabledAndCityAndAddress(0, city, address);
    }

    // findByCityAndAddress Tests
    @Test
    void testFindByCityAndAddress_WithExistingCustomers() {
        // Arrange
        String city = "New York";
        String address = "123 Main St";
        when(customerRepository.findByCityAndAddress(city, address)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByCityAndAddress(city, address);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Customer>) result).size());
        verify(customerRepository, times(1)).findByCityAndAddress(city, address);
    }

    @Test
    void testFindByCityAndAddress_WithNonExistingCustomers() {
        // Arrange
        String city = "Boston";
        String address = "789 Pine Rd";
        when(customerRepository.findByCityAndAddress(city, address)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findByCityAndAddress(city, address);

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(customerRepository, times(1)).findByCityAndAddress(city, address);
    }

    // saveCustomer Tests
    @Test
    void testSaveCustomer_WithValidCustomer() {
        // Arrange
        Customer newCustomer = Customer.builder()
                .name("NewCompany")
                .email("new@company.com")
                .enabled(0)
                .build();
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        // Act
        customerService.saveCustomer(newCustomer);

        // Assert
        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    void testSaveCustomer_EnabledFlagIsSetToOne() {
        // Arrange
        Customer disabledCustomer = Customer.builder()
                .name("DisabledCompany")
                .email("disabled@company.com")
                .enabled(0)
                .build();
        when(customerRepository.save(any(Customer.class))).thenReturn(disabledCustomer);

        // Act
        customerService.saveCustomer(disabledCustomer);

        // Assert
        assertEquals(1, disabledCustomer.getEnabled());
        verify(customerRepository, times(1)).save(disabledCustomer);
    }

    @Test
    void testSaveCustomer_WithNullCustomer() {
        // Arrange
        when(customerRepository.save(null)).thenThrow(new IllegalArgumentException("Customer cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.saveCustomer(null);
        });
        verify(customerRepository, times(1)).save(null);
    }

    @Test
    void testSaveCustomer_WithAllFields() {
        // Arrange
        Customer fullCustomer = Customer.builder()
                .name("FullCustomer")
                .email("full@customer.com")
                .phone(111222333)
                .firstName("Full")
                .lastName("Customer")
                .city("Full City")
                .address("Full Address")
                .categories(testCategories)
                .enabled(0)
                .build();
        when(customerRepository.save(any(Customer.class))).thenReturn(fullCustomer);

        // Act
        customerService.saveCustomer(fullCustomer);

        // Assert
        assertEquals(1, fullCustomer.getEnabled());
        verify(customerRepository, times(1)).save(fullCustomer);
    }

    @Test
    void testSaveCustomer_MultipleTimes() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        customerService.saveCustomer(testCustomer);
        customerService.saveCustomer(testCustomer);

        // Assert
        verify(customerRepository, times(2)).save(testCustomer);
    }
}
