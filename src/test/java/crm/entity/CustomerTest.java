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
    public void testCustomerConstructor() {
        assertNotNull(customer);
    }

    @Test
    public void testCustomerBuilder() {
        Customer builtCustomer = Customer.builder()
                .id(1L)
                .name("Test Company")
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
        assertEquals("Test Company", builtCustomer.getName());
    }

    @Test
    public void testSetAndGetId() {
        customer.setId(1L);
        assertEquals(1L, customer.getId());
    }

    @Test
    public void testSetAndGetName() {
        customer.setName("TestCompany");
        assertEquals("TestCompany", customer.getName());
    }

    @Test
    public void testSetAndGetEmail() {
        customer.setEmail("test@example.com");
        assertEquals("test@example.com", customer.getEmail());
    }

    @Test
    public void testSetAndGetPhone() {
        customer.setPhone(123456789);
        assertEquals(123456789, customer.getPhone());
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
        customer.setCity("TestCity");
        assertEquals("TestCity", customer.getCity());
    }

    @Test
    public void testSetAndGetAddress() {
        customer.setAddress("123 Test St");
        assertEquals("123 Test St", customer.getAddress());
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
        category.setId(1L);
        category.setName("VIP");
        categories.add(category);

        customer.setCategories(categories);
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    public void testCustomerWithNullValues() {
        customer.setName(null);
        customer.setEmail(null);
        customer.setFirstName(null);
        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getFirstName());
    }

    @Test
    public void testCustomerWithEmptyStrings() {
        customer.setName("");
        customer.setEmail("");
        assertEquals("", customer.getName());
        assertEquals("", customer.getEmail());
    }

    @Test
    public void testCustomerPhoneBoundary() {
        customer.setPhone(0);
        assertEquals(0, customer.getPhone());
        customer.setPhone(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, customer.getPhone());
    }

    @Test
    public void testCustomerEnabledValues() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    public void testCustomerAllArgsConstructor() {
        Set<Category> categories = new HashSet<>();
        Customer allArgsCustomer = new Customer(1L, "Company", "test@test.com", 123, categories, "John", "Doe", "City", "Address", 1);
        assertNotNull(allArgsCustomer);
        assertEquals("Company", allArgsCustomer.getName());
        assertEquals("test@test.com", allArgsCustomer.getEmail());
    }
}
