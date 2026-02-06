package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
    void testDefaultConstructor() {
        assertNotNull(user);
    }

    @Test
    void testBuilderPattern() {
        User builtUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();

        assertNotNull(builtUser);
        assertEquals(1L, builtUser.getId());
        assertEquals("testuser", builtUser.getUsername());
        assertEquals("test@example.com", builtUser.getEmail());
        assertEquals("John", builtUser.getFirstName());
        assertEquals("Doe", builtUser.getLastName());
        assertEquals("password123", builtUser.getPassword());
        assertEquals(1, builtUser.getEnabled());
        assertEquals(role, builtUser.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1L, "johndoe", "john@example.com",
                "John", "Doe", "pass123", 1, role);

        assertEquals(1L, user.getId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("pass123", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertEquals(role, user.getRole());
    }

    @Test
    void testGettersAndSetters() {
        user.setId(1L);
        user.setUsername("janesmith");
        user.setEmail("jane@example.com");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setPassword("securePass");
        user.setEnabled(1);
        user.setRole(role);

        assertEquals(1L, user.getId());
        assertEquals("janesmith", user.getUsername());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("securePass", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertEquals(role, user.getRole());
    }

    @Test
    void testGetName() {
        user.setFirstName("John");
        user.setLastName("Doe");

        String fullName = user.getName();

        assertEquals("John Doe", fullName);
    }

    @Test
    void testGetNameWithNullValues() {
        user.setFirstName(null);
        user.setLastName(null);

        String fullName = user.getName();

        assertEquals("null null", fullName);
    }

    @Test
    void testGetRoleId() {
        user.setRole(role);

        assertEquals(1, user.getRole_id());
    }

    @Test
    void testGetRoleName() {
        user.setRole(role);

        assertEquals("ROLE_USER", user.getRole_name());
    }

    @Test
    void testGetColumnCount() {
        int columnCount = user.getColumnCount();

        assertTrue(columnCount > 0);
    }

    @Test
    void testEnabledFlag() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());

        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testNullValues() {
        user.setId(null);
        user.setUsername(null);
        user.setEmail(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setPassword(null);
        user.setRole(null);

        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    @Test
    void testEmailValidation() {
        String validEmail = "user@example.com";
        user.setEmail(validEmail);

        assertEquals(validEmail, user.getEmail());
        assertTrue(user.getEmail().contains("@"));
    }

    @Test
    void testRoleRelationship() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        user.setRole(adminRole);

        assertNotNull(user.getRole());
        assertEquals(2, user.getRole().getId());
        assertEquals("ROLE_ADMIN", user.getRole().getName());
    }

    @Test
    void testEquality() {
        User user1 = User.builder()
                .id(1L)
                .username("test")
                .email("test@example.com")
                .build();

        User user2 = User.builder()
                .id(1L)
                .username("test")
                .email("test@example.com")
                .build();

        assertEquals(user1, user2);
    }

    @Test
    void testHashCode() {
        user.setId(1L);
        user.setUsername("test");

        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testToString() {
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");

        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("johndoe"));
    }

    @Test
    void testRoleIdWithNullRole() {
        user.setRole(null);

        assertThrows(NullPointerException.class, () -> {
            user.getRole_id();
        });
    }

    @Test
    void testRoleNameWithNullRole() {
        user.setRole(null);

        assertThrows(NullPointerException.class, () -> {
            user.getRole_name();
        });
    }
}
