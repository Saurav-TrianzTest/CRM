package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Entity Tests")
class CustomerTest {

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("VIP");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Premium");

        categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);

        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("customer@test.com")
                .phone(1234567890)
                .firstName("John")
                .lastName("Smith")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .categories(categories)
                .build();
    }

    @Test
    @DisplayName("Test Customer builder creates valid instance")
    void testCustomerBuilder() {
        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("Test Customer", customer.getName());
        assertEquals("customer@test.com", customer.getEmail());
        assertEquals(1234567890, customer.getPhone());
        assertEquals("John", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("New York", customer.getCity());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals(1, customer.getEnabled());
        assertNotNull(customer.getCategories());
        assertEquals(2, customer.getCategories().size());
    }

    @Test
    @DisplayName("Test Customer no-args constructor")
    void testNoArgsConstructor() {
        Customer emptyCustomer = new Customer();
        assertNotNull(emptyCustomer);
        assertNull(emptyCustomer.getId());
        assertNull(emptyCustomer.getName());
    }

    @Test
    @DisplayName("Test Customer all-args constructor")
    void testAllArgsConstructor() {
        Customer newCustomer = new Customer(
                2L,
                "New Customer",
                "new@test.com",
                987654321,
                categories,
                "Jane",
                "Doe",
                "Boston",
                "456 Oak Ave",
                1
        );

        assertNotNull(newCustomer);
        assertEquals(2L, newCustomer.getId());
        assertEquals("New Customer", newCustomer.getName());
        assertEquals("new@test.com", newCustomer.getEmail());
    }

    @Test
    @DisplayName("Test Customer getters and setters")
    void testGettersAndSetters() {
        Customer testCustomer = new Customer();
        testCustomer.setId(3L);
        testCustomer.setName("Updated Customer");
        testCustomer.setEmail("updated@test.com");
        testCustomer.setPhone(111222333);
        testCustomer.setFirstName("Updated");
        testCustomer.setLastName("Name");
        testCustomer.setCity("Chicago");
        testCustomer.setAddress("789 Pine Rd");
        testCustomer.setEnabled(0);
        testCustomer.setCategories(categories);

        assertEquals(3L, testCustomer.getId());
        assertEquals("Updated Customer", testCustomer.getName());
        assertEquals("updated@test.com", testCustomer.getEmail());
        assertEquals(111222333, testCustomer.getPhone());
        assertEquals("Updated", testCustomer.getFirstName());
        assertEquals("Name", testCustomer.getLastName());
        assertEquals("Chicago", testCustomer.getCity());
        assertEquals("789 Pine Rd", testCustomer.getAddress());
        assertEquals(0, testCustomer.getEnabled());
        assertEquals(categories, testCustomer.getCategories());
    }

    @Test
    @DisplayName("Test Customer with null values")
    void testCustomerWithNullValues() {
        Customer nullCustomer = Customer.builder()
                .name(null)
                .email(null)
                .firstName(null)
                .lastName(null)
                .city(null)
                .address(null)
                .categories(null)
                .build();

        assertNotNull(nullCustomer);
        assertNull(nullCustomer.getName());
        assertNull(nullCustomer.getEmail());
        assertNull(nullCustomer.getFirstName());
        assertNull(nullCustomer.getLastName());
        assertNull(nullCustomer.getCity());
        assertNull(nullCustomer.getAddress());
        assertNull(nullCustomer.getCategories());
    }

    @Test
    @DisplayName("Test Customer with empty categories")
    void testCustomerWithEmptyCategories() {
        customer.setCategories(new HashSet<>());
        assertNotNull(customer.getCategories());
        assertEquals(0, customer.getCategories().size());
    }

    @Test
    @DisplayName("Test Customer add category")
    void testAddCategory() {
        Category newCategory = new Category();
        newCategory.setId(3L);
        newCategory.setName("Gold");

        Set<Category> currentCategories = customer.getCategories();
        currentCategories.add(newCategory);
        customer.setCategories(currentCategories);

        assertEquals(3, customer.getCategories().size());
        assertTrue(customer.getCategories().contains(newCategory));
    }

    @Test
    @DisplayName("Test Customer remove category")
    void testRemoveCategory() {
        Set<Category> currentCategories = customer.getCategories();
        Category toRemove = currentCategories.iterator().next();
        currentCategories.remove(toRemove);
        customer.setCategories(currentCategories);

        assertEquals(1, customer.getCategories().size());
        assertFalse(customer.getCategories().contains(toRemove));
    }

    @Test
    @DisplayName("Test Customer phone with zero")
    void testPhoneWithZero() {
        customer.setPhone(0);
        assertEquals(0, customer.getPhone());
    }

    @Test
    @DisplayName("Test Customer phone with negative value")
    void testPhoneWithNegative() {
        customer.setPhone(-123456);
        assertEquals(-123456, customer.getPhone());
    }

    @Test
    @DisplayName("Test Customer phone with max integer")
    void testPhoneWithMaxValue() {
        customer.setPhone(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, customer.getPhone());
    }

    @Test
    @DisplayName("Test Customer enabled status")
    void testEnabledStatus() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());

        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    @DisplayName("Test Customer email format")
    void testEmailFormat() {
        customer.setEmail("valid.email@domain.com");
        assertEquals("valid.email@domain.com", customer.getEmail());
    }

    @Test
    @DisplayName("Test Customer with empty email")
    void testEmptyEmail() {
        customer.setEmail("");
        assertEquals("", customer.getEmail());
    }

    @Test
    @DisplayName("Test Customer name minimum size")
    void testNameMinimumSize() {
        customer.setName("AB");
        assertEquals("AB", customer.getName());
        assertEquals(2, customer.getName().length());
    }

    @Test
    @DisplayName("Test Customer name with single character")
    void testNameWithSingleCharacter() {
        customer.setName("A");
        assertEquals("A", customer.getName());
    }

    @Test
    @DisplayName("Test Customer equals and hashCode")
    void testEqualsAndHashCode() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .email("cust1@test.com")
                .build();

        Customer customer2 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .email("cust1@test.com")
                .build();

        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    @DisplayName("Test Customer toString")
    void testToString() {
        String customerString = customer.toString();
        assertNotNull(customerString);
        assertTrue(customerString.contains("Test Customer"));
        assertTrue(customerString.contains("customer@test.com"));
    }

    @Test
    @DisplayName("Test Customer with multiple categories")
    void testMultipleCategories() {
        Set<Category> multiCategories = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            Category cat = new Category();
            cat.setId((long) i);
            cat.setName("Category " + i);
            multiCategories.add(cat);
        }

        customer.setCategories(multiCategories);
        assertEquals(5, customer.getCategories().size());
    }

    @Test
    @DisplayName("Test Customer address with special characters")
    void testAddressWithSpecialCharacters() {
        customer.setAddress("123 Main St, Apt #4B");
        assertEquals("123 Main St, Apt #4B", customer.getAddress());
    }
}
