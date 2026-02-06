package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    void testCustomerCreation() {
        assertNotNull(customer);
    }

    @Test
    void testCustomerBuilder() {
        Customer builtCustomer = Customer.builder()
                .id(1L)
                .name("TestCompany")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("TestCity")
                .address("123 Test St")
                .enabled(1)
                .build();

        assertNotNull(builtCustomer);
        assertEquals(1L, builtCustomer.getId());
        assertEquals("TestCompany", builtCustomer.getName());
        assertEquals("test@example.com", builtCustomer.getEmail());
    }

    @Test
    void testSetAndGetId() {
        customer.setId(1L);
        assertEquals(1L, customer.getId());
    }

    @Test
    void testSetAndGetName() {
        customer.setName("TestCompany");
        assertEquals("TestCompany", customer.getName());
    }

    @Test
    void testSetAndGetEmail() {
        customer.setEmail("test@example.com");
        assertEquals("test@example.com", customer.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        customer.setPhone(123456789);
        assertEquals(123456789, customer.getPhone());
    }

    @Test
    void testSetAndGetFirstName() {
        customer.setFirstName("John");
        assertEquals("John", customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        customer.setLastName("Doe");
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    void testSetAndGetCity() {
        customer.setCity("TestCity");
        assertEquals("TestCity", customer.getCity());
    }

    @Test
    void testSetAndGetAddress() {
        customer.setAddress("123 Test St");
        assertEquals("123 Test St", customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testSetAndGetCategories() {
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Premium");
        categories.add(category);

        customer.setCategories(categories);
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testCustomerWithNullValues() {
        customer.setName(null);
        customer.setEmail(null);
        customer.setFirstName(null);

        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getFirstName());
    }

    @Test
    void testCustomerWithEmptyCategories() {
        customer.setCategories(new HashSet<>());
        assertNotNull(customer.getCategories());
        assertTrue(customer.getCategories().isEmpty());
    }

    @Test
    void testCustomerEquality() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("TestCompany")
                .email("test@example.com")
                .build();

        Customer customer2 = Customer.builder()
                .id(1L)
                .name("TestCompany")
                .email("test@example.com")
                .build();

        assertEquals(customer1, customer2);
    }

    @Test
    void testAllArgsConstructor() {
        Set<Category> categories = new HashSet<>();
        Customer customer = new Customer(1L, "TestCompany", "test@example.com", 123456789,
                categories, "John", "Doe", "TestCity", "123 Test St", 1);

        assertNotNull(customer);
        assertEquals("TestCompany", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
    }

    @Test
    void testNoArgsConstructor() {
        Customer customer = new Customer();
        assertNotNull(customer);
    }
}
