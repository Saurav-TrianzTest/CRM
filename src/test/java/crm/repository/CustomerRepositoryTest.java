package crm.repository;

import crm.entity.Category;
import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("CustomerRepository Tests")
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("VIP");
        entityManager.persist(category);
        entityManager.flush();
    }

    @Test
    @DisplayName("Test CustomerRepository extends JpaRepository")
    void testCustomerRepositoryExtendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(CustomerRepository.class));
    }

    @Test
    @DisplayName("Test CustomerRepository is not null")
    void testCustomerRepositoryNotNull() {
        assertNotNull(customerRepository);
    }

    @Test
    @DisplayName("Test getMaxId returns maximum customer id")
    void testGetMaxId() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .email("cust1@example.com")
                .phone(111111111)
                .enabled(1)
                .build();
        entityManager.persist(customer1);

        Customer customer2 = Customer.builder()
                .name("Customer2")
                .email("cust2@example.com")
                .phone(222222222)
                .enabled(1)
                .build();
        entityManager.persist(customer2);
        entityManager.flush();

        Long maxId = customerRepository.getMaxId();
        assertNotNull(maxId);
        assertTrue(maxId > 0);
    }

    @Test
    @DisplayName("Test findAllByEnabled returns only enabled customers")
    void testFindAllByEnabled() {
        Customer enabled = Customer.builder()
                .name("Enabled")
                .email("enabled@example.com")
                .phone(111111111)
                .enabled(1)
                .build();
        entityManager.persist(enabled);

        Customer disabled = Customer.builder()
                .name("Disabled")
                .email("disabled@example.com")
                .phone(222222222)
                .enabled(0)
                .build();
        entityManager.persist(disabled);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findAllByEnabled(1);
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
        assertTrue(customerList.stream().allMatch(c -> c.getEnabled() == 1));
    }

    @Test
    @DisplayName("Test findOneByEnabledAndName returns correct customer")
    void testFindOneByEnabledAndName() {
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Customer found = customerRepository.findOneByEnabledAndName(1, "Test Customer");
        assertNotNull(found);
        assertEquals("Test Customer", found.getName());
        assertEquals(1, found.getEnabled());
    }

    @Test
    @DisplayName("Test findOneByName returns customer regardless of enabled status")
    void testFindOneByName() {
        Customer customer = Customer.builder()
                .name("Any Customer")
                .email("any@example.com")
                .phone(123456789)
                .enabled(0)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Customer found = customerRepository.findOneByName("Any Customer");
        assertNotNull(found);
        assertEquals("Any Customer", found.getName());
    }

    @Test
    @DisplayName("Test findByEnabledAndEmail returns customers by email and enabled status")
    void testFindByEnabledAndEmail() {
        Customer customer = Customer.builder()
                .name("Email Customer")
                .email("email@example.com")
                .phone(123456789)
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByEnabledAndEmail(1, "email@example.com");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
        assertTrue(customerList.stream().allMatch(c -> c.getEmail().equals("email@example.com") && c.getEnabled() == 1));
    }

    @Test
    @DisplayName("Test findByEmail returns customers by email regardless of status")
    void testFindByEmail() {
        Customer customer = Customer.builder()
                .name("Email Customer")
                .email("any@example.com")
                .phone(123456789)
                .enabled(0)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByEmail("any@example.com");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
    }

    @Test
    @DisplayName("Test findByEnabledAndCity returns customers by city and enabled status")
    void testFindByEnabledAndCity() {
        Customer customer = Customer.builder()
                .name("City Customer")
                .email("city@example.com")
                .phone(123456789)
                .city("New York")
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByEnabledAndCity(1, "New York");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
        assertTrue(customerList.stream().allMatch(c -> c.getCity().equals("New York")));
    }

    @Test
    @DisplayName("Test findByCity returns customers by city regardless of status")
    void testFindByCity() {
        Customer customer = Customer.builder()
                .name("City Customer")
                .email("city@example.com")
                .phone(123456789)
                .city("Boston")
                .enabled(0)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByCity("Boston");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
    }

    @Test
    @DisplayName("Test findByEnabledAndCityAndAddress returns customers by city, address and status")
    void testFindByEnabledAndCityAndAddress() {
        Customer customer = Customer.builder()
                .name("Address Customer")
                .email("addr@example.com")
                .phone(123456789)
                .city("Chicago")
                .address("123 Main St")
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByEnabledAndCityAndAddress(1, "Chicago", "123 Main St");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
    }

    @Test
    @DisplayName("Test findByPhone returns customers by phone")
    void testFindByPhone() {
        Customer customer = Customer.builder()
                .name("Phone Customer")
                .email("phone@example.com")
                .phone(999888777)
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByPhone(999888777);
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
        assertTrue(customerList.stream().allMatch(c -> c.getPhone() == 999888777));
    }

    @Test
    @DisplayName("Test findByEnabledAndFirstName returns customers by first name and status")
    void testFindByEnabledAndFirstName() {
        Customer customer = Customer.builder()
                .name("Name Customer")
                .email("name@example.com")
                .phone(123456789)
                .firstName("John")
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByEnabledAndFirstName(1, "John");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
        assertTrue(customerList.stream().allMatch(c -> c.getFirstName().equals("John")));
    }

    @Test
    @DisplayName("Test findByLastName returns customers by last name")
    void testFindByLastName() {
        Customer customer = Customer.builder()
                .name("Last Name Customer")
                .email("last@example.com")
                .phone(123456789)
                .lastName("Doe")
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByLastName("Doe");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
        assertTrue(customerList.stream().allMatch(c -> c.getLastName().equals("Doe")));
    }

    @Test
    @DisplayName("Test findByFirstNameAndLastName returns customers by full name")
    void testFindByFirstNameAndLastName() {
        Customer customer = Customer.builder()
                .name("Full Name Customer")
                .email("full@example.com")
                .phone(123456789)
                .firstName("Jane")
                .lastName("Smith")
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByFirstNameAndLastName("Jane", "Smith");
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
    }

    @Test
    @DisplayName("Test findByCategories returns customers by category")
    void testFindByCategories() {
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Customer customer = Customer.builder()
                .name("Category Customer")
                .email("cat@example.com")
                .phone(123456789)
                .categories(categories)
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();

        Iterable<Customer> customers = customerRepository.findByCategories(categories);
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        assertFalse(customerList.isEmpty());
    }

    @Test
    @DisplayName("Test save customer")
    void testSaveCustomer() {
        Customer customer = Customer.builder()
                .name("New Customer")
                .email("new@example.com")
                .phone(111222333)
                .enabled(1)
                .build();

        Customer saved = customerRepository.save(customer);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("New Customer", saved.getName());
    }

    @Test
    @DisplayName("Test delete customer")
    void testDeleteCustomer() {
        Customer customer = Customer.builder()
                .name("Delete Customer")
                .email("delete@example.com")
                .phone(123456789)
                .enabled(1)
                .build();
        Customer saved = entityManager.persist(customer);
        entityManager.flush();

        Long customerId = saved.getId();
        customerRepository.deleteById(customerId);

        Customer deleted = customerRepository.findById(customerId).orElse(null);
        assertNull(deleted);
    }
}
