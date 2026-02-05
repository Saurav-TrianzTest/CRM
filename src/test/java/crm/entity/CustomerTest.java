package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
    }

    @Test
    public void testCustomerCreation() {
        assertNotNull(customer);
    }

    @Test
    public void testBuilderPattern() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("Test Customer", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
    }

    @Test
    public void testSetAndGetId() {
        customer.setId(100L);
        assertEquals(100L, customer.getId());
    }

    @Test
    public void testSetAndGetName() {
        customer.setName("Customer Name");
        assertEquals("Customer Name", customer.getName());
    }

    @Test
    public void testSetAndGetEmail() {
        customer.setEmail("email@test.com");
        assertEquals("email@test.com", customer.getEmail());
    }

    @Test
    public void testSetAndGetPhone() {
        customer.setPhone(555123);
        assertEquals(555123, customer.getPhone());
    }

    @Test
    public void testSetAndGetFirstName() {
        customer.setFirstName("John");
        assertEquals("John", customer.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        customer.setLastName("Doe");
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    public void testSetAndGetCity() {
        customer.setCity("Boston");
        assertEquals("Boston", customer.getCity());
    }

    @Test
    public void testSetAndGetAddress() {
        customer.setAddress("456 Elm St");
        assertEquals("456 Elm St", customer.getAddress());
    }

    @Test
    public void testSetAndGetEnabled() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    public void testSetAndGetCategories() {
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setName("VIP");
        categories.add(category);

        customer.setCategories(categories);
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    public void testCustomerWithNullFields() {
        customer.setName(null);
        customer.setEmail(null);
        customer.setFirstName(null);

        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getFirstName());
    }

    @Test
    public void testCustomerEquality() {
        Customer c1 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();

        Customer c2 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();

        assertEquals(c1, c2);
    }

    @Test
    public void testCustomerHashCode() {
        customer.setId(1L);
        customer.setName("Test");
        int hashCode = customer.hashCode();
        assertTrue(hashCode != 0);
    }
}
