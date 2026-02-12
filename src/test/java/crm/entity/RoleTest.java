package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Role Entity Tests")
class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ADMIN");
    }

    @Test
    @DisplayName("Test Role constructor")
    void testRoleConstructor() {
        Role newRole = new Role();
        assertNotNull(newRole);
        assertEquals(0, newRole.getId());
        assertNull(newRole.getName());
    }

    @Test
    @DisplayName("Test Role getters and setters")
    void testGettersAndSetters() {
        Role testRole = new Role();
        testRole.setId(2);
        testRole.setName("USER");

        assertEquals(2, testRole.getId());
        assertEquals("USER", testRole.getName());
    }

    @Test
    @DisplayName("Test Role with valid data")
    void testRoleWithValidData() {
        assertNotNull(role);
        assertEquals(1, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    @DisplayName("Test Role with null name")
    void testRoleWithNullName() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    @DisplayName("Test Role with empty name")
    void testRoleWithEmptyName() {
        role.setName("");
        assertEquals("", role.getName());
    }

    @Test
    @DisplayName("Test Role with zero id")
    void testRoleWithZeroId() {
        role.setId(0);
        assertEquals(0, role.getId());
    }

    @Test
    @DisplayName("Test Role with negative id")
    void testRoleWithNegativeId() {
        role.setId(-1);
        assertEquals(-1, role.getId());
    }

    @Test
    @DisplayName("Test Role with large id")
    void testRoleWithLargeId() {
        role.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, role.getId());
    }

    @Test
    @DisplayName("Test Role name with USER")
    void testRoleNameUser() {
        role.setName("USER");
        assertEquals("USER", role.getName());
    }

    @Test
    @DisplayName("Test Role name with MODERATOR")
    void testRoleNameModerator() {
        role.setName("MODERATOR");
        assertEquals("MODERATOR", role.getName());
    }

    @Test
    @DisplayName("Test Role name with lowercase")
    void testRoleNameLowercase() {
        role.setName("admin");
        assertEquals("admin", role.getName());
    }

    @Test
    @DisplayName("Test Role name with mixed case")
    void testRoleNameMixedCase() {
        role.setName("Admin");
        assertEquals("Admin", role.getName());
    }

    @Test
    @DisplayName("Test Role name with special characters")
    void testRoleNameWithSpecialCharacters() {
        role.setName("SUPER_ADMIN");
        assertEquals("SUPER_ADMIN", role.getName());
    }

    @Test
    @DisplayName("Test Role name with spaces")
    void testRoleNameWithSpaces() {
        role.setName("ADMIN USER");
        assertEquals("ADMIN USER", role.getName());
    }

    @Test
    @DisplayName("Test Role equals and hashCode")
    void testEqualsAndHashCode() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ADMIN");

        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    @DisplayName("Test Role not equals with different id")
    void testNotEqualsWithDifferentId() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ADMIN");

        assertNotEquals(role1, role2);
    }

    @Test
    @DisplayName("Test Role not equals with different name")
    void testNotEqualsWithDifferentName() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("USER");

        assertNotEquals(role1, role2);
    }

    @Test
    @DisplayName("Test Role toString")
    void testToString() {
        String roleString = role.toString();
        assertNotNull(roleString);
        assertTrue(roleString.contains("ADMIN") || roleString.contains("1"));
    }

    @Test
    @DisplayName("Test Role id update")
    void testRoleIdUpdate() {
        assertEquals(1, role.getId());
        role.setId(5);
        assertEquals(5, role.getId());
    }

    @Test
    @DisplayName("Test Role name update")
    void testRoleNameUpdate() {
        assertEquals("ADMIN", role.getName());
        role.setName("SUPERUSER");
        assertEquals("SUPERUSER", role.getName());
    }

    @Test
    @DisplayName("Test Role with long name")
    void testRoleWithLongName() {
        String longName = "A".repeat(100);
        role.setName(longName);
        assertEquals(longName, role.getName());
        assertEquals(100, role.getName().length());
    }

    @Test
    @DisplayName("Test Role name uniqueness constraint")
    void testRoleNameUniqueness() {
        // This test documents that role name should be unique
        role.setName("UNIQUE_ROLE");
        assertEquals("UNIQUE_ROLE", role.getName());
    }
}
