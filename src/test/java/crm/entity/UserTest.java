package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new User();
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    void constructor_shouldCreateUser() {
        // Assert
        assertNotNull(user);
    }

    @Test
    void builder_shouldCreateUserWithAllFields() {
        // Act
        User built = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();
        
        // Assert
        assertNotNull(built);
        assertEquals(1L, built.getId());
        assertEquals("testuser", built.getUsername());
        assertEquals("test@example.com", built.getEmail());
        assertEquals("John", built.getFirstName());
        assertEquals("Doe", built.getLastName());
        assertEquals("password123", built.getPassword());
        assertEquals(1, built.getEnabled());
        assertEquals(role, built.getRole());
    }

    @Test
    void setId_shouldSetId() {
        // Act
        user.setId(1L);
        
        // Assert
        assertEquals(1L, user.getId());
    }

    @Test
    void setUsername_shouldSetUsername() {
        // Act
        user.setUsername("testuser");
        
        // Assert
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void setEmail_shouldSetEmail() {
        // Act
        user.setEmail("test@example.com");
        
        // Assert
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        // Act
        user.setFirstName("John");
        
        // Assert
        assertEquals("John", user.getFirstName());
    }

    @Test
    void setLastName_shouldSetLastName() {
        // Act
        user.setLastName("Doe");
        
        // Assert
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void setPassword_shouldSetPassword() {
        // Act
        user.setPassword("password123");
        
        // Assert
        assertEquals("password123", user.getPassword());
    }

    @Test
    void setEnabled_shouldSetEnabled() {
        // Act
        user.setEnabled(1);
        
        // Assert
        assertEquals(1, user.getEnabled());
    }

    @Test
    void setRole_shouldSetRole() {
        // Act
        user.setRole(role);
        
        // Assert
        assertEquals(role, user.getRole());
    }

    @Test
    void getColumnCount_shouldReturnFieldCount() {
        // Act
        int count = user.getColumnCount();
        
        // Assert
        assertTrue(count > 0);
    }

    @Test
    void getRole_id_shouldReturnRoleId() {
        // Arrange
        user.setRole(role);
        
        // Act
        int roleId = user.getRole_id();
        
        // Assert
        assertEquals(1, roleId);
    }

    @Test
    void getRole_name_shouldReturnRoleName() {
        // Arrange
        user.setRole(role);
        
        // Act
        String roleName = user.getRole_name();
        
        // Assert
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void getName_shouldReturnFullName() {
        // Arrange
        user.setFirstName("John");
        user.setLastName("Doe");
        
        // Act
        String name = user.getName();
        
        // Assert
        assertEquals("John Doe", name);
    }

    @Test
    void allArgsConstructor_shouldCreateUserWithAllFields() {
        // Act
        User user = new User(1L, "testuser", "test@example.com", 
            "John", "Doe", "password", 1, role);
        
        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyUser() {
        // Act
        User user = new User();
        
        // Assert
        assertNotNull(user);
    }
}
