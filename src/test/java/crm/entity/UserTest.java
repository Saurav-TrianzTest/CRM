package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        user = new User();
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    public void testUserConstructor() {
        assertNotNull(user);
    }

    @Test
    public void testUserBuilder() {
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
        assertEquals("testuser", builtUser.getUsername());
        assertEquals("test@example.com", builtUser.getEmail());
    }

    @Test
    public void testSetAndGetId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    public void testSetAndGetUsername() {
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testSetAndGetEmail() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testSetAndGetFirstName() {
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testSetAndGetPassword() {
        user.setPassword("password123");
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testSetAndGetEnabled() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    public void testSetAndGetRole() {
        user.setRole(role);
        assertNotNull(user.getRole());
        assertEquals(1, user.getRole().getId());
        assertEquals("ROLE_USER", user.getRole().getName());
    }

    @Test
    public void testGetColumnCount() {
        int columnCount = user.getColumnCount();
        assertTrue(columnCount > 0);
    }

    @Test
    public void testGetRoleId() {
        user.setRole(role);
        assertEquals(1, user.getRole_id());
    }

    @Test
    public void testGetRoleName() {
        user.setRole(role);
        assertEquals("ROLE_USER", user.getRole_name());
    }

    @Test
    public void testGetName() {
        user.setFirstName("John");
        user.setLastName("Doe");
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testGetNameWithNullValues() {
        user.setFirstName(null);
        user.setLastName(null);
        assertEquals("null null", user.getName());
    }

    @Test
    public void testUserWithNullEmail() {
        user.setEmail(null);
        assertNull(user.getEmail());
    }

    @Test
    public void testUserEnabledValues() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    public void testUserAllArgsConstructor() {
        User allArgsUser = new User(1L, "testuser", "test@test.com", "John", "Doe", "pass", 1, role);
        assertNotNull(allArgsUser);
        assertEquals("testuser", allArgsUser.getUsername());
        assertEquals("test@test.com", allArgsUser.getEmail());
    }
}
