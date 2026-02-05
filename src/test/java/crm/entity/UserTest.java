package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user);
    }

    @Test
    public void testBuilderPattern() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .build();

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testSetAndGetId() {
        user.setId(100L);
        assertEquals(100L, user.getId());
    }

    @Test
    public void testSetAndGetUsername() {
        user.setUsername("johndoe");
        assertEquals("johndoe", user.getUsername());
    }

    @Test
    public void testSetAndGetEmail() {
        user.setEmail("john@test.com");
        assertEquals("john@test.com", user.getEmail());
    }

    @Test
    public void testSetAndGetFirstName() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    public void testSetAndGetPassword() {
        user.setPassword("securePass");
        assertEquals("securePass", user.getPassword());
    }

    @Test
    public void testSetAndGetEnabled() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    public void testSetAndGetRole() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        user.setRole(role);

        assertNotNull(user.getRole());
        assertEquals(1, user.getRole().getId());
    }

    @Test
    public void testGetColumnCount() {
        int count = user.getColumnCount();
        assertTrue(count > 0);
    }

    @Test
    public void testGetRoleId() {
        Role role = new Role();
        role.setId(5);
        role.setName("ROLE_ADMIN");
        user.setRole(role);

        assertEquals(5, user.getRole_id());
    }

    @Test
    public void testGetRoleName() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_MANAGER");
        user.setRole(role);

        assertEquals("ROLE_MANAGER", user.getRole_name());
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
    public void testUserEquality() {
        User u1 = User.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .build();

        User u2 = User.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .build();

        assertEquals(u1, u2);
    }

    @Test
    public void testUserHashCode() {
        user.setId(1L);
        user.setUsername("test");
        int hashCode = user.hashCode();
        assertTrue(hashCode != 0);
    }
}
