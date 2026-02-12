package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity Tests")
class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    @DisplayName("Test User builder creates valid instance")
    void testUserBuilder() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertNotNull(user.getRole());
    }

    @Test
    @DisplayName("Test User no-args constructor")
    void testNoArgsConstructor() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
        assertNull(emptyUser.getId());
        assertNull(emptyUser.getUsername());
    }

    @Test
    @DisplayName("Test User all-args constructor")
    void testAllArgsConstructor() {
        User newUser = new User(
                2L,
                "newuser",
                "new@example.com",
                "Jane",
                "Smith",
                "newpass",
                1,
                role
        );

        assertNotNull(newUser);
        assertEquals(2L, newUser.getId());
        assertEquals("newuser", newUser.getUsername());
        assertEquals("new@example.com", newUser.getEmail());
    }

    @Test
    @DisplayName("Test User getColumnCount method")
    void testGetColumnCount() {
        int columnCount = user.getColumnCount();
        assertTrue(columnCount > 0);
        assertEquals(8, columnCount);
    }

    @Test
    @DisplayName("Test User getRole_id method")
    void testGetRoleId() {
        assertEquals(1, user.getRole_id());
    }

    @Test
    @DisplayName("Test User getRole_id throws NPE when role is null")
    void testGetRoleIdWithNullRole() {
        user.setRole(null);
        assertThrows(NullPointerException.class, () -> user.getRole_id());
    }

    @Test
    @DisplayName("Test User getRole_name method")
    void testGetRoleName() {
        assertEquals("ADMIN", user.getRole_name());
    }

    @Test
    @DisplayName("Test User getRole_name throws NPE when role is null")
    void testGetRoleNameWithNullRole() {
        user.setRole(null);
        assertThrows(NullPointerException.class, () -> user.getRole_name());
    }

    @Test
    @DisplayName("Test User getName method")
    void testGetName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    @DisplayName("Test User getName with null firstName")
    void testGetNameWithNullFirstName() {
        user.setFirstName(null);
        String name = user.getName();
        assertTrue(name.contains("Doe"));
    }

    @Test
    @DisplayName("Test User getName with null lastName")
    void testGetNameWithNullLastName() {
        user.setLastName(null);
        String name = user.getName();
        assertTrue(name.contains("John"));
    }

    @Test
    @DisplayName("Test User getName with both names null")
    void testGetNameWithBothNamesNull() {
        user.setFirstName(null);
        user.setLastName(null);
        String name = user.getName();
        assertNotNull(name);
    }

    @Test
    @DisplayName("Test User getters and setters")
    void testGettersAndSetters() {
        User testUser = new User();
        testUser.setId(3L);
        testUser.setUsername("updateuser");
        testUser.setEmail("update@example.com");
        testUser.setFirstName("Updated");
        testUser.setLastName("User");
        testUser.setPassword("newpass");
        testUser.setEnabled(0);
        testUser.setRole(role);

        assertEquals(3L, testUser.getId());
        assertEquals("updateuser", testUser.getUsername());
        assertEquals("update@example.com", testUser.getEmail());
        assertEquals("Updated", testUser.getFirstName());
        assertEquals("User", testUser.getLastName());
        assertEquals("newpass", testUser.getPassword());
        assertEquals(0, testUser.getEnabled());
        assertEquals(role, testUser.getRole());
    }

    @Test
    @DisplayName("Test User with enabled = 0")
    void testUserDisabled() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    @DisplayName("Test User with enabled = 1")
    void testUserEnabled() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    @DisplayName("Test User email validation format")
    void testEmailFormat() {
        user.setEmail("valid@email.com");
        assertEquals("valid@email.com", user.getEmail());
    }

    @Test
    @DisplayName("Test User with empty email")
    void testEmptyEmail() {
        user.setEmail("");
        assertEquals("", user.getEmail());
    }

    @Test
    @DisplayName("Test User equals and hashCode")
    void testEqualsAndHashCode() {
        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@test.com")
                .build();

        User user2 = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@test.com")
                .build();

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Test User toString")
    void testToString() {
        String userString = user.toString();
        assertNotNull(userString);
        assertTrue(userString.contains("testuser"));
        assertTrue(userString.contains("test@example.com"));
    }

    @Test
    @DisplayName("Test User role relationship")
    void testRoleRelationship() {
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("USER");

        user.setRole(newRole);
        assertEquals(newRole, user.getRole());
        assertEquals(2, user.getRole_id());
        assertEquals("USER", user.getRole_name());
    }

    @Test
    @DisplayName("Test User password can be null")
    void testPasswordNull() {
        user.setPassword(null);
        assertNull(user.getPassword());
    }

    @Test
    @DisplayName("Test User with special characters in name")
    void testSpecialCharactersInName() {
        user.setFirstName("Jean-Pierre");
        user.setLastName("O'Brien");
        assertEquals("Jean-Pierre O'Brien", user.getName());
    }
}
