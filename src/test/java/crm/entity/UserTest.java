package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("User Entity Tests")
class UserTest {

    @Mock
    private Role mockRole;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }

    @Test
    @DisplayName("Test default constructor creates non-null instance")
    void testDefaultConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
    }

    @Test
    @DisplayName("Test all-args constructor with all fields")
    void testAllArgsConstructor() {
        Long id = 1L;
        String username = "testuser";
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password123";
        int enabled = 1;

        User user = new User(id, username, email, firstName, lastName, password, enabled, mockRole);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(password, user.getPassword());
        assertEquals(enabled, user.getEnabled());
        assertEquals(mockRole, user.getRole());
    }

    @Test
    @DisplayName("Test builder pattern with all fields")
    void testBuilderWithAllFields() {
        Long id = 2L;
        String username = "builderuser";
        String email = "builder@example.com";
        String firstName = "Jane";
        String lastName = "Smith";
        String password = "securepass";
        int enabled = 1;

        User user = User.builder()
                .id(id)
                .username(username)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .enabled(enabled)
                .role(mockRole)
                .build();

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(password, user.getPassword());
        assertEquals(enabled, user.getEnabled());
        assertEquals(mockRole, user.getRole());
    }

    @Test
    @DisplayName("Test builder with minimal fields")
    void testBuilderWithMinimalFields() {
        User user = User.builder()
                .username("minimaluser")
                .email("minimal@example.com")
                .build();

        assertNotNull(user);
        assertEquals("minimaluser", user.getUsername());
        assertEquals("minimal@example.com", user.getEmail());
        assertNull(user.getId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
    }

    @Test
    @DisplayName("Test setId and getId")
    void testSetAndGetId() {
        Long id = 10L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    @DisplayName("Test setId with null value")
    void testSetIdNull() {
        user.setId(null);
        assertNull(user.getId());
    }

    @Test
    @DisplayName("Test setUsername and getUsername")
    void testSetAndGetUsername() {
        String username = "johndoe";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    @DisplayName("Test setUsername with null value")
    void testSetUsernameNull() {
        user.setUsername(null);
        assertNull(user.getUsername());
    }

    @Test
    @DisplayName("Test setUsername with empty string")
    void testSetUsernameEmpty() {
        String username = "";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    @DisplayName("Test setEmail and getEmail")
    void testSetAndGetEmail() {
        String email = "john.doe@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Test setEmail with null value")
    void testSetEmailNull() {
        user.setEmail(null);
        assertNull(user.getEmail());
    }

    @Test
    @DisplayName("Test setEmail with invalid format")
    void testSetEmailInvalidFormat() {
        String email = "invalid-email";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Test setFirstName and getFirstName")
    void testSetAndGetFirstName() {
        String firstName = "John";
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    @DisplayName("Test setFirstName with null value")
    void testSetFirstNameNull() {
        user.setFirstName(null);
        assertNull(user.getFirstName());
    }

    @Test
    @DisplayName("Test setLastName and getLastName")
    void testSetAndGetLastName() {
        String lastName = "Doe";
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    @DisplayName("Test setLastName with null value")
    void testSetLastNameNull() {
        user.setLastName(null);
        assertNull(user.getLastName());
    }

    @Test
    @DisplayName("Test setPassword and getPassword")
    void testSetAndGetPassword() {
        String password = "secretPassword123!";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("Test setPassword with null value")
    void testSetPasswordNull() {
        user.setPassword(null);
        assertNull(user.getPassword());
    }

    @Test
    @DisplayName("Test setPassword with empty string")
    void testSetPasswordEmpty() {
        String password = "";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("Test setEnabled and getEnabled with 1")
    void testSetAndGetEnabledOne() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    @DisplayName("Test setEnabled and getEnabled with 0")
    void testSetAndGetEnabledZero() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    @DisplayName("Test setEnabled with negative value")
    void testSetEnabledNegative() {
        user.setEnabled(-1);
        assertEquals(-1, user.getEnabled());
    }

    @Test
    @DisplayName("Test setRole and getRole")
    void testSetAndGetRole() {
        user.setRole(mockRole);
        assertEquals(mockRole, user.getRole());
    }

    @Test
    @DisplayName("Test setRole with null")
    void testSetRoleNull() {
        user.setRole(null);
        assertNull(user.getRole());
    }

    @Test
    @DisplayName("Test getColumnCount returns field count")
    void testGetColumnCount() {
        int columnCount = user.getColumnCount();
        assertTrue(columnCount > 0);
        assertEquals(8, columnCount);
    }

    @Test
    @DisplayName("Test getRole_id returns role id")
    void testGetRoleId() {
        when(mockRole.getId()).thenReturn(5);
        user.setRole(mockRole);

        int roleId = user.getRole_id();

        assertEquals(5, roleId);
        verify(mockRole, times(1)).getId();
    }

    @Test
    @DisplayName("Test getRole_id throws NullPointerException when role is null")
    void testGetRoleIdWithNullRole() {
        user.setRole(null);
        assertThrows(NullPointerException.class, () -> user.getRole_id());
    }

    @Test
    @DisplayName("Test getRole_name returns role name")
    void testGetRoleName() {
        when(mockRole.getName()).thenReturn("ADMIN");
        user.setRole(mockRole);

        String roleName = user.getRole_name();

        assertEquals("ADMIN", roleName);
        verify(mockRole, times(1)).getName();
    }

    @Test
    @DisplayName("Test getRole_name throws NullPointerException when role is null")
    void testGetRoleNameWithNullRole() {
        user.setRole(null);
        assertThrows(NullPointerException.class, () -> user.getRole_name());
    }

    @Test
    @DisplayName("Test getName returns full name")
    void testGetName() {
        user.setFirstName("John");
        user.setLastName("Doe");

        String fullName = user.getName();

        assertEquals("John Doe", fullName);
    }

    @Test
    @DisplayName("Test getName with null firstName")
    void testGetNameWithNullFirstName() {
        user.setFirstName(null);
        user.setLastName("Doe");

        String fullName = user.getName();

        assertEquals("null Doe", fullName);
    }

    @Test
    @DisplayName("Test getName with null lastName")
    void testGetNameWithNullLastName() {
        user.setFirstName("John");
        user.setLastName(null);

        String fullName = user.getName();

        assertEquals("John null", fullName);
    }

    @Test
    @DisplayName("Test getName with both names null")
    void testGetNameWithBothNull() {
        user.setFirstName(null);
        user.setLastName(null);

        String fullName = user.getName();

        assertEquals("null null", fullName);
    }

    @Test
    @DisplayName("Test getName with empty strings")
    void testGetNameWithEmptyStrings() {
        user.setFirstName("");
        user.setLastName("");

        String fullName = user.getName();

        assertEquals(" ", fullName);
    }

    @Test
    @DisplayName("Test equals with same object")
    void testEqualsWithSameObject() {
        user.setId(1L);
        user.setUsername("testuser");
        assertEquals(user, user);
    }

    @Test
    @DisplayName("Test equals with equal objects")
    void testEqualsWithEqualObjects() {
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        User other = new User();
        other.setId(1L);
        other.setUsername("testuser");
        other.setEmail("test@example.com");

        assertEquals(user, other);
    }

    @Test
    @DisplayName("Test equals with different objects")
    void testEqualsWithDifferentObjects() {
        user.setId(1L);
        user.setUsername("testuser");

        User other = new User();
        other.setId(2L);
        other.setUsername("otheruser");

        assertNotEquals(user, other);
    }

    @Test
    @DisplayName("Test equals with null")
    void testEqualsWithNull() {
        user.setId(1L);
        assertNotEquals(null, user);
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        user.setId(1L);
        user.setUsername("testuser");

        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test hashCode with equal objects")
    void testHashCodeWithEqualObjects() {
        user.setId(1L);
        user.setUsername("testuser");

        User other = new User();
        other.setId(1L);
        other.setUsername("testuser");

        assertEquals(user.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test toString contains field values")
    void testToString() {
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("test@example.com"));
    }

    @Test
    @DisplayName("Test user with long username")
    void testUserWithLongUsername() {
        String longUsername = "a".repeat(255);
        user.setUsername(longUsername);
        assertEquals(longUsername, user.getUsername());
        assertEquals(255, user.getUsername().length());
    }

    @Test
    @DisplayName("Test user with special characters in username")
    void testUserWithSpecialCharactersInUsername() {
        String username = "user_name-123.test";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    @DisplayName("Test user with complex email")
    void testUserWithComplexEmail() {
        String email = "first.last+tag@sub.example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Test complete user lifecycle")
    void testCompleteUserLifecycle() {
        User newUser = User.builder()
                .username("lifecycle")
                .email("lifecycle@example.com")
                .firstName("Life")
                .lastName("Cycle")
                .password("password")
                .enabled(0)
                .role(mockRole)
                .build();

        assertNotNull(newUser);
        assertEquals(0, newUser.getEnabled());

        newUser.setEnabled(1);
        assertEquals(1, newUser.getEnabled());

        newUser.setPassword("newSecurePassword!");
        assertEquals("newSecurePassword!", newUser.getPassword());
    }

    @Test
    @DisplayName("Test user with multiple role changes")
    void testUserWithMultipleRoleChanges() {
        Role role1 = mock(Role.class);
        Role role2 = mock(Role.class);

        user.setRole(role1);
        assertEquals(role1, user.getRole());

        user.setRole(role2);
        assertEquals(role2, user.getRole());

        user.setRole(null);
        assertNull(user.getRole());
    }
}
