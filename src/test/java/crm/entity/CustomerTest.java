package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Tech");
        categories.add(category);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(customer);
    }

    @Test
    void testBuilderConstructor() {
        Customer customerWithBuilder = Customer.builder()
                .id(1L)
                .name("Company A")
                .email("company@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .categories(categories)
                .build();

        assertEquals(1L, customerWithBuilder.getId());
        assertEquals("Company A", customerWithBuilder.getName());
    }

    @Test
    void testAllArgsConstructor() {
        Customer customerWithArgs = new Customer(1L, "Company B", "test@test.com", 987654321,
                categories, "Jane", "Smith", "Boston", "456 Oak Ave", 1);

        assertEquals(1L, customerWithArgs.getId());
        assertEquals("Company B", customerWithArgs.getName());
    }

    @Test
    void testSetAndGetId() {
        customer.setId(10L);
        assertEquals(10L, customer.getId());
    }

    @Test
    void testSetAndGetName() {
        customer.setName("Test Company");
        assertEquals("Test Company", customer.getName());
    }

    @Test
    void testSetAndGetEmail() {
        customer.setEmail("customer@test.com");
        assertEquals("customer@test.com", customer.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        customer.setPhone(555123456);
        assertEquals(555123456, customer.getPhone());
    }

    @Test
    void testSetAndGetFirstName() {
        customer.setFirstName("Alice");
        assertEquals("Alice", customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        customer.setLastName("Johnson");
        assertEquals("Johnson", customer.getLastName());
    }

    @Test
    void testSetAndGetCity() {
        customer.setCity("Chicago");
        assertEquals("Chicago", customer.getCity());
    }

    @Test
    void testSetAndGetAddress() {
        customer.setAddress("789 Pine St");
        assertEquals("789 Pine St", customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testSetAndGetCategories() {
        customer.setCategories(categories);
        assertEquals(categories, customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testPhoneWithZero() {
        customer.setPhone(0);
        assertEquals(0, customer.getPhone());
    }

    @Test
    void testEnabledDisabled() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }
}
