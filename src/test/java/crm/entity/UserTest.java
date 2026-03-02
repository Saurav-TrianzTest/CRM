package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testDefaultConstructor() {
        assertNotNull(user);
    }

    @Test
    void testBuilderConstructor() {
        User userWithBuilder = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();

        assertEquals(1L, userWithBuilder.getId());
        assertEquals("testuser", userWithBuilder.getUsername());
        assertEquals("test@example.com", userWithBuilder.getEmail());
    }

    @Test
    void testAllArgsConstructor() {
        User userWithArgs = new User(1L, "user1", "user@test.com", "Jane", "Smith", "pass", 1, role);

        assertEquals(1L, userWithArgs.getId());
        assertEquals("user1", userWithArgs.getUsername());
        assertEquals("user@test.com", userWithArgs.getEmail());
    }

    @Test
    void testSetAndGetId() {
        user.setId(5L);
        assertEquals(5L, user.getId());
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("johndoe");
        assertEquals("johndoe", user.getUsername());
    }

    @Test
    void testSetAndGetEmail() {
        user.setEmail("john@example.com");
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testSetAndGetFirstName() {
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void testSetAndGetPassword() {
        user.setPassword("securepass");
        assertEquals("securepass", user.getPassword());
    }

    @Test
    void testSetAndGetEnabled() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testSetAndGetRole() {
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void testGetColumnCount() {
        assertTrue(user.getColumnCount() > 0);
    }

    @Test
    void testGetRole_id() {
        user.setRole(role);
        assertEquals(1, user.getRole_id());
    }

    @Test
    void testGetRole_name() {
        user.setRole(role);
        assertEquals("ROLE_USER", user.getRole_name());
    }

    @Test
    void testGetName() {
        user.setFirstName("John");
        user.setLastName("Doe");
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testGetNameWithNullValues() {
        assertEquals("null null", user.getName());
    }

    @Test
    void testEnabledFalse() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }
}
