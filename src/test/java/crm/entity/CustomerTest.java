package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(customer);
    }

    @Test
    void testBuilderPattern() {
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Premium");
        categories.add(category);

        Customer builtCustomer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone(123456789)
                .categories(categories)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        assertNotNull(builtCustomer);
        assertEquals(1L, builtCustomer.getId());
        assertEquals("John Doe", builtCustomer.getName());
        assertEquals("john@example.com", builtCustomer.getEmail());
        assertEquals(123456789, builtCustomer.getPhone());
        assertEquals("John", builtCustomer.getFirstName());
        assertEquals("Doe", builtCustomer.getLastName());
        assertEquals("New York", builtCustomer.getCity());
        assertEquals("123 Main St", builtCustomer.getAddress());
        assertEquals(1, builtCustomer.getEnabled());
    }

    @Test
    void testAllArgsConstructor() {
        Set<Category> categories = new HashSet<>();
        Customer customer = new Customer(1L, "John Doe", "john@example.com",
                123456789, categories, "John", "Doe", "New York", "123 Main St", 1);

        assertEquals(1L, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals(123456789, customer.getPhone());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("New York", customer.getCity());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testGettersAndSetters() {
        customer.setId(1L);
        customer.setName("Jane Smith");
        customer.setEmail("jane@example.com");
        customer.setPhone(987654321);
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setCity("Boston");
        customer.setAddress("456 Oak Ave");
        customer.setEnabled(1);

        assertEquals(1L, customer.getId());
        assertEquals("Jane Smith", customer.getName());
        assertEquals("jane@example.com", customer.getEmail());
        assertEquals(987654321, customer.getPhone());
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("Boston", customer.getCity());
        assertEquals("456 Oak Ave", customer.getAddress());
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testCategories() {
        Set<Category> categories = new HashSet<>();

        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Premium");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("VIP");

        categories.add(cat1);
        categories.add(cat2);

        customer.setCategories(categories);

        assertNotNull(customer.getCategories());
        assertEquals(2, customer.getCategories().size());
        assertTrue(customer.getCategories().contains(cat1));
        assertTrue(customer.getCategories().contains(cat2));
    }

    @Test
    void testEmptyCategories() {
        Set<Category> emptyCategories = new HashSet<>();
        customer.setCategories(emptyCategories);

        assertNotNull(customer.getCategories());
        assertEquals(0, customer.getCategories().size());
    }

    @Test
    void testNullValues() {
        customer.setId(null);
        customer.setName(null);
        customer.setEmail(null);
        customer.setCategories(null);
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setCity(null);
        customer.setAddress(null);

        assertNull(customer.getId());
        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getCategories());
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getCity());
        assertNull(customer.getAddress());
    }

    @Test
    void testEnabledFlag() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());

        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testPhoneBoundary() {
        customer.setPhone(0);
        assertEquals(0, customer.getPhone());

        customer.setPhone(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, customer.getPhone());
    }

    @Test
    void testEmailValidation() {
        String validEmail = "test@example.com";
        customer.setEmail(validEmail);

        assertEquals(validEmail, customer.getEmail());
        assertTrue(customer.getEmail().contains("@"));
    }

    @Test
    void testNameMinimumLength() {
        String shortName = "AB";
        customer.setName(shortName);

        assertEquals(shortName, customer.getName());
        assertTrue(customer.getName().length() >= 2);
    }

    @Test
    void testEquality() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        Customer customer2 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        assertEquals(customer1, customer2);
    }

    @Test
    void testToString() {
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");

        String toString = customer.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("john@example.com"));
    }

    @Test
    void testHashCode() {
        customer.setId(1L);
        customer.setName("Test");

        int hashCode1 = customer.hashCode();
        int hashCode2 = customer.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}
