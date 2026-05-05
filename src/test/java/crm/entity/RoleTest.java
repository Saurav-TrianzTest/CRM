package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void constructor_shouldCreateRole() {
        // Assert
        assertNotNull(role);
    }

    @Test
    void setId_shouldSetId() {
        // Act
        role.setId(1);
        
        // Assert
        assertEquals(1, role.getId());
    }

    @Test
    void setName_shouldSetName() {
        // Act
        role.setName("ROLE_ADMIN");
        
        // Assert
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void getId_shouldReturnId() {
        // Arrange
        role.setId(5);
        
        // Act
        int id = role.getId();
        
        // Assert
        assertEquals(5, id);
    }

    @Test
    void getName_shouldReturnName() {
        // Arrange
        role.setName("ROLE_USER");
        
        // Act
        String name = role.getName();
        
        // Assert
        assertEquals("ROLE_USER", name);
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");
        
        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");
        
        // Assert
        assertEquals(role1, role2);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");
        
        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");
        
        // Assert
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void toString_shouldReturnStringRepresentation() {
        // Arrange
        role.setId(1);
        role.setName("ROLE_ADMIN");
        
        // Act
        String result = role.toString();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("ROLE_ADMIN"));
    }
}
