package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
    void constructor_shouldCreateCustomer() {
        // Assert
        assertNotNull(customer);
    }

    @Test
    void builder_shouldCreateCustomerWithAllFields() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        
        // Act
        Customer built = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .categories(categories)
                .build();
        
        // Assert
        assertNotNull(built);
        assertEquals(1L, built.getId());
        assertEquals("Test Customer", built.getName());
        assertEquals("test@example.com", built.getEmail());
        assertEquals(123456789, built.getPhone());
        assertEquals("John", built.getFirstName());
        assertEquals("Doe", built.getLastName());
        assertEquals("New York", built.getCity());
        assertEquals("123 Main St", built.getAddress());
        assertEquals(1, built.getEnabled());
    }

    @Test
    void setId_shouldSetId() {
        // Act
        customer.setId(1L);
        
        // Assert
        assertEquals(1L, customer.getId());
    }

    @Test
    void setName_shouldSetName() {
        // Act
        customer.setName("Test Name");
        
        // Assert
        assertEquals("Test Name", customer.getName());
    }

    @Test
    void setEmail_shouldSetEmail() {
        // Act
        customer.setEmail("test@example.com");
        
        // Assert
        assertEquals("test@example.com", customer.getEmail());
    }

    @Test
    void setPhone_shouldSetPhone() {
        // Act
        customer.setPhone(123456789);
        
        // Assert
        assertEquals(123456789, customer.getPhone());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        // Act
        customer.setFirstName("John");
        
        // Assert
        assertEquals("John", customer.getFirstName());
    }

    @Test
    void setLastName_shouldSetLastName() {
        // Act
        customer.setLastName("Doe");
        
        // Assert
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    void setCity_shouldSetCity() {
        // Act
        customer.setCity("New York");
        
        // Assert
        assertEquals("New York", customer.getCity());
    }

    @Test
    void setAddress_shouldSetAddress() {
        // Act
        customer.setAddress("123 Main St");
        
        // Assert
        assertEquals("123 Main St", customer.getAddress());
    }

    @Test
    void setEnabled_shouldSetEnabled() {
        // Act
        customer.setEnabled(1);
        
        // Assert
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void setCategories_shouldSetCategories() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setName("Test Category");
        categories.add(category);
        
        // Act
        customer.setCategories(categories);
        
        // Assert
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void allArgsConstructor_shouldCreateCustomerWithAllFields() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        
        // Act
        Customer customer = new Customer(1L, "Test", "test@example.com", 
            123456789, categories, "John", "Doe", "NYC", "123 St", 1);
        
        // Assert
        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("Test", customer.getName());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyCustomer() {
        // Act
        Customer customer = new Customer();
        
        // Assert
        assertNotNull(customer);
    }
}
