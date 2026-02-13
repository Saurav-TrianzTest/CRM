package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Entity Tests")
class CustomerTest {

    @Mock
    private Category mockCategory1;

    @Mock
    private Category mockCategory2;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
    }

    @Test
    @DisplayName("Test default constructor creates non-null instance")
    void testDefaultConstructor() {
        Customer newCustomer = new Customer();
        assertNotNull(newCustomer);
    }

    @Test
    @DisplayName("Test all-args constructor with all fields")
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Acme Corp";
        String email = "contact@acme.com";
        int phone = 1234567890;
        Set<Category> categories = new HashSet<>();
        String firstName = "John";
        String lastName = "Doe";
        String city = "New York";
        String address = "123 Main St";
        int enabled = 1;

        Customer customer = new Customer(id, name, email, phone, categories, firstName, lastName, city, address, enabled);

        assertNotNull(customer);
        assertEquals(id, customer.getId());
        assertEquals(name, customer.getName());
        assertEquals(email, customer.getEmail());
        assertEquals(phone, customer.getPhone());
        assertEquals(categories, customer.getCategories());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        assertEquals(city, customer.getCity());
        assertEquals(address, customer.getAddress());
        assertEquals(enabled, customer.getEnabled());
    }

    @Test
    @DisplayName("Test builder pattern with all fields")
    void testBuilderWithAllFields() {
        Long id = 2L;
        String name = "TechCorp";
        String email = "info@techcorp.com";
        int phone = 987654321;
        Set<Category> categories = new HashSet<>();
        categories.add(mockCategory1);
        String firstName = "Jane";
        String lastName = "Smith";
        String city = "San Francisco";
        String address = "456 Tech Ave";
        int enabled = 1;

        Customer customer = Customer.builder()
                .id(id)
                .name(name)
                .email(email)
                .phone(phone)
                .categories(categories)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .address(address)
                .enabled(enabled)
                .build();

        assertNotNull(customer);
        assertEquals(id, customer.getId());
        assertEquals(name, customer.getName());
        assertEquals(email, customer.getEmail());
        assertEquals(phone, customer.getPhone());
        assertEquals(categories, customer.getCategories());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        assertEquals(city, customer.getCity());
        assertEquals(address, customer.getAddress());
        assertEquals(enabled, customer.getEnabled());
    }

    @Test
    @DisplayName("Test builder with minimal fields")
    void testBuilderWithMinimalFields() {
        Customer customer = Customer.builder()
                .name("MinimalCorp")
                .email("minimal@corp.com")
                .build();

        assertNotNull(customer);
        assertEquals("MinimalCorp", customer.getName());
        assertEquals("minimal@corp.com", customer.getEmail());
        assertNull(customer.getId());
        assertEquals(0, customer.getPhone());
        assertEquals(0, customer.getEnabled());
    }

    @Test
    @DisplayName("Test setId and getId")
    void testSetAndGetId() {
        Long id = 10L;
        customer.setId(id);
        assertEquals(id, customer.getId());
    }

    @Test
    @DisplayName("Test setId with null value")
    void testSetIdNull() {
        customer.setId(null);
        assertNull(customer.getId());
    }

    @Test
    @DisplayName("Test setName and getName")
    void testSetAndGetName() {
        String name = "Enterprise Solutions Inc";
        customer.setName(name);
        assertEquals(name, customer.getName());
    }

    @Test
    @DisplayName("Test setName with null value")
    void testSetNameNull() {
        customer.setName(null);
        assertNull(customer.getName());
    }

    @Test
    @DisplayName("Test setName with empty string")
    void testSetNameEmpty() {
        String name = "";
        customer.setName(name);
        assertEquals(name, customer.getName());
    }

    @Test
    @DisplayName("Test setName with single character")
    void testSetNameSingleCharacter() {
        String name = "A";
        customer.setName(name);
        assertEquals(name, customer.getName());
    }

    @Test
    @DisplayName("Test setName with two characters (minimum size)")
    void testSetNameTwoCharacters() {
        String name = "AB";
        customer.setName(name);
        assertEquals(name, customer.getName());
    }

    @Test
    @DisplayName("Test setEmail and getEmail")
    void testSetAndGetEmail() {
        String email = "customer@example.com";
        customer.setEmail(email);
        assertEquals(email, customer.getEmail());
    }

    @Test
    @DisplayName("Test setEmail with null value")
    void testSetEmailNull() {
        customer.setEmail(null);
        assertNull(customer.getEmail());
    }

    @Test
    @DisplayName("Test setEmail with invalid format")
    void testSetEmailInvalidFormat() {
        String email = "invalid-email";
        customer.setEmail(email);
        assertEquals(email, customer.getEmail());
    }

    @Test
    @DisplayName("Test setEmail with complex valid email")
    void testSetEmailComplex() {
        String email = "first.last+tag@sub.example.com";
        customer.setEmail(email);
        assertEquals(email, customer.getEmail());
    }

    @Test
    @DisplayName("Test setPhone and getPhone")
    void testSetAndGetPhone() {
        int phone = 1234567890;
        customer.setPhone(phone);
        assertEquals(phone, customer.getPhone());
    }

    @Test
    @DisplayName("Test setPhone with zero")
    void testSetPhoneZero() {
        customer.setPhone(0);
        assertEquals(0, customer.getPhone());
    }

    @Test
    @DisplayName("Test setPhone with negative value")
    void testSetPhoneNegative() {
        int phone = -1234567890;
        customer.setPhone(phone);
        assertEquals(phone, customer.getPhone());
    }

    @Test
    @DisplayName("Test setPhone with maximum integer")
    void testSetPhoneMaximum() {
        int phone = Integer.MAX_VALUE;
        customer.setPhone(phone);
        assertEquals(phone, customer.getPhone());
    }

    @Test
    @DisplayName("Test setCategories and getCategories")
    void testSetAndGetCategories() {
        Set<Category> categories = new HashSet<>();
        categories.add(mockCategory1);
        categories.add(mockCategory2);

        customer.setCategories(categories);

        assertEquals(categories, customer.getCategories());
        assertEquals(2, customer.getCategories().size());
    }

    @Test
    @DisplayName("Test setCategories with null")
    void testSetCategoriesNull() {
        customer.setCategories(null);
        assertNull(customer.getCategories());
    }

    @Test
    @DisplayName("Test setCategories with empty set")
    void testSetCategoriesEmpty() {
        Set<Category> categories = new HashSet<>();
        customer.setCategories(categories);

        assertNotNull(customer.getCategories());
        assertEquals(0, customer.getCategories().size());
    }

    @Test
    @DisplayName("Test adding categories incrementally")
    void testAddCategoriesIncrementally() {
        Set<Category> categories = new HashSet<>();
        customer.setCategories(categories);

        categories.add(mockCategory1);
        assertEquals(1, customer.getCategories().size());

        categories.add(mockCategory2);
        assertEquals(2, customer.getCategories().size());
    }

    @Test
    @DisplayName("Test setFirstName and getFirstName")
    void testSetAndGetFirstName() {
        String firstName = "Robert";
        customer.setFirstName(firstName);
        assertEquals(firstName, customer.getFirstName());
    }

    @Test
    @DisplayName("Test setFirstName with null value")
    void testSetFirstNameNull() {
        customer.setFirstName(null);
        assertNull(customer.getFirstName());
    }

    @Test
    @DisplayName("Test setLastName and getLastName")
    void testSetAndGetLastName() {
        String lastName = "Johnson";
        customer.setLastName(lastName);
        assertEquals(lastName, customer.getLastName());
    }

    @Test
    @DisplayName("Test setLastName with null value")
    void testSetLastNameNull() {
        customer.setLastName(null);
        assertNull(customer.getLastName());
    }

    @Test
    @DisplayName("Test setCity and getCity")
    void testSetAndGetCity() {
        String city = "Los Angeles";
        customer.setCity(city);
        assertEquals(city, customer.getCity());
    }

    @Test
    @DisplayName("Test setCity with null value")
    void testSetCityNull() {
        customer.setCity(null);
        assertNull(customer.getCity());
    }

    @Test
    @DisplayName("Test setCity with empty string")
    void testSetCityEmpty() {
        String city = "";
        customer.setCity(city);
        assertEquals(city, customer.getCity());
    }

    @Test
    @DisplayName("Test setAddress and getAddress")
    void testSetAndGetAddress() {
        String address = "789 Business Blvd, Suite 100";
        customer.setAddress(address);
        assertEquals(address, customer.getAddress());
    }

    @Test
    @DisplayName("Test setAddress with null value")
    void testSetAddressNull() {
        customer.setAddress(null);
        assertNull(customer.getAddress());
    }

    @Test
    @DisplayName("Test setAddress with multiline address")
    void testSetAddressMultiline() {
        String address = "Building 5\nFloor 3\nRoom 302";
        customer.setAddress(address);
        assertEquals(address, customer.getAddress());
    }

    @Test
    @DisplayName("Test setEnabled and getEnabled with 1")
    void testSetAndGetEnabledOne() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    @DisplayName("Test setEnabled and getEnabled with 0")
    void testSetAndGetEnabledZero() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    @DisplayName("Test setEnabled with negative value")
    void testSetEnabledNegative() {
        customer.setEnabled(-1);
        assertEquals(-1, customer.getEnabled());
    }

    @Test
    @DisplayName("Test equals with same object")
    void testEqualsWithSameObject() {
        customer.setId(1L);
        customer.setName("Test Corp");
        assertEquals(customer, customer);
    }

    @Test
    @DisplayName("Test equals with equal objects")
    void testEqualsWithEqualObjects() {
        customer.setId(1L);
        customer.setName("Test Corp");
        customer.setEmail("test@corp.com");

        Customer other = new Customer();
        other.setId(1L);
        other.setName("Test Corp");
        other.setEmail("test@corp.com");

        assertEquals(customer, other);
    }

    @Test
    @DisplayName("Test equals with different objects")
    void testEqualsWithDifferentObjects() {
        customer.setId(1L);
        customer.setName("Test Corp");

        Customer other = new Customer();
        other.setId(2L);
        other.setName("Other Corp");

        assertNotEquals(customer, other);
    }

    @Test
    @DisplayName("Test equals with null")
    void testEqualsWithNull() {
        customer.setId(1L);
        assertNotEquals(null, customer);
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        customer.setId(1L);
        customer.setName("Test Corp");

        int hashCode1 = customer.hashCode();
        int hashCode2 = customer.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test hashCode with equal objects")
    void testHashCodeWithEqualObjects() {
        customer.setId(1L);
        customer.setName("Test Corp");

        Customer other = new Customer();
        other.setId(1L);
        other.setName("Test Corp");

        assertEquals(customer.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test toString contains field values")
    void testToString() {
        customer.setId(1L);
        customer.setName("Test Corp");
        customer.setEmail("test@corp.com");
        customer.setCity("Boston");

        String toString = customer.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Test Corp"));
        assertTrue(toString.contains("test@corp.com"));
    }

    @Test
    @DisplayName("Test customer with long name")
    void testCustomerWithLongName() {
        String longName = "A".repeat(255);
        customer.setName(longName);
        assertEquals(longName, customer.getName());
        assertEquals(255, customer.getName().length());
    }

    @Test
    @DisplayName("Test customer with special characters in name")
    void testCustomerWithSpecialCharactersInName() {
        String name = "Smith & Johnson, LLC.";
        customer.setName(name);
        assertEquals(name, customer.getName());
    }

    @Test
    @DisplayName("Test customer with international phone number")
    void testCustomerWithInternationalPhone() {
        int phone = 123456789;
        customer.setPhone(phone);
        assertEquals(phone, customer.getPhone());
    }

    @Test
    @DisplayName("Test complete customer lifecycle")
    void testCompleteCustomerLifecycle() {
        Customer newCustomer = Customer.builder()
                .name("Lifecycle Corp")
                .email("lifecycle@corp.com")
                .phone(1234567890)
                .firstName("John")
                .lastName("Lifecycle")
                .city("Chicago")
                .address("100 Lifecycle St")
                .enabled(0)
                .build();

        assertNotNull(newCustomer);
        assertEquals(0, newCustomer.getEnabled());

        newCustomer.setEnabled(1);
        assertEquals(1, newCustomer.getEnabled());

        newCustomer.setAddress("200 New Address Ave");
        assertEquals("200 New Address Ave", newCustomer.getAddress());
    }

    @Test
    @DisplayName("Test customer with multiple category changes")
    void testCustomerWithMultipleCategoryChanges() {
        Set<Category> categories1 = new HashSet<>();
        categories1.add(mockCategory1);

        customer.setCategories(categories1);
        assertEquals(1, customer.getCategories().size());

        Set<Category> categories2 = new HashSet<>();
        categories2.add(mockCategory1);
        categories2.add(mockCategory2);

        customer.setCategories(categories2);
        assertEquals(2, customer.getCategories().size());
    }

    @Test
    @DisplayName("Test customer with unicode characters in name")
    void testCustomerWithUnicodeInName() {
        String name = "Société Française";
        customer.setName(name);
        assertEquals(name, customer.getName());
    }

    @Test
    @DisplayName("Test customer with unicode characters in city")
    void testCustomerWithUnicodeInCity() {
        String city = "São Paulo";
        customer.setCity(city);
        assertEquals(city, customer.getCity());
    }

    @Test
    @DisplayName("Test customer with all optional fields null")
    void testCustomerWithAllOptionalFieldsNull() {
        customer.setName("Required Name");
        customer.setEmail("required@email.com");

        assertNull(customer.getId());
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getCity());
        assertNull(customer.getAddress());
        assertNull(customer.getCategories());
    }

    @Test
    @DisplayName("Test customer enabled state transitions")
    void testCustomerEnabledStateTransitions() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());

        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());

        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }
}
