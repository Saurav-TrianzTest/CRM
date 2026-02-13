package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Role Entity Tests")
class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    @DisplayName("Test default constructor creates non-null instance")
    void testDefaultConstructor() {
        Role newRole = new Role();
        assertNotNull(newRole);
    }

    @Test
    @DisplayName("Test setId and getId")
    void testSetAndGetId() {
        int id = 1;
        role.setId(id);
        assertEquals(id, role.getId());
    }

    @Test
    @DisplayName("Test setId with zero")
    void testSetIdZero() {
        role.setId(0);
        assertEquals(0, role.getId());
    }

    @Test
    @DisplayName("Test setId with negative value")
    void testSetIdNegative() {
        int id = -1;
        role.setId(id);
        assertEquals(id, role.getId());
    }

    @Test
    @DisplayName("Test setId with maximum integer")
    void testSetIdMaximum() {
        int id = Integer.MAX_VALUE;
        role.setId(id);
        assertEquals(id, role.getId());
    }

    @Test
    @DisplayName("Test setId with minimum integer")
    void testSetIdMinimum() {
        int id = Integer.MIN_VALUE;
        role.setId(id);
        assertEquals(id, role.getId());
    }

    @Test
    @DisplayName("Test setId with various positive values")
    void testSetIdVariousPositiveValues() {
        int[] ids = {1, 5, 10, 100, 1000};
        for (int id : ids) {
            role.setId(id);
            assertEquals(id, role.getId());
        }
    }

    @Test
    @DisplayName("Test setName and getName")
    void testSetAndGetName() {
        String name = "ADMIN";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with null value")
    void testSetNameNull() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    @DisplayName("Test setName with empty string")
    void testSetNameEmpty() {
        String name = "";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with USER role")
    void testSetNameUser() {
        String name = "USER";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with MANAGER role")
    void testSetNameManager() {
        String name = "MANAGER";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with ADMIN role")
    void testSetNameAdmin() {
        String name = "ADMIN";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with lowercase role")
    void testSetNameLowercase() {
        String name = "admin";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with mixed case role")
    void testSetNameMixedCase() {
        String name = "Administrator";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with long string")
    void testSetNameLongString() {
        String name = "ROLE_WITH_VERY_LONG_NAME_" + "X".repeat(200);
        role.setName(name);
        assertEquals(name, role.getName());
        assertTrue(role.getName().length() > 200);
    }

    @Test
    @DisplayName("Test setName with special characters")
    void testSetNameSpecialCharacters() {
        String name = "ROLE_ADMIN_2024";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with underscores")
    void testSetNameWithUnderscores() {
        String name = "SUPER_ADMIN";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with spaces")
    void testSetNameWithSpaces() {
        String name = "System Administrator";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test setName with prefix ROLE_")
    void testSetNameWithRolePrefix() {
        String name = "ROLE_ADMIN";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test equals with same object")
    void testEqualsWithSameObject() {
        role.setId(1);
        role.setName("ADMIN");
        assertEquals(role, role);
    }

    @Test
    @DisplayName("Test equals with equal objects")
    void testEqualsWithEqualObjects() {
        role.setId(1);
        role.setName("ADMIN");

        Role other = new Role();
        other.setId(1);
        other.setName("ADMIN");

        assertEquals(role, other);
    }

    @Test
    @DisplayName("Test equals with different ids")
    void testEqualsWithDifferentIds() {
        role.setId(1);
        role.setName("ADMIN");

        Role other = new Role();
        other.setId(2);
        other.setName("ADMIN");

        assertNotEquals(role, other);
    }

    @Test
    @DisplayName("Test equals with different names")
    void testEqualsWithDifferentNames() {
        role.setId(1);
        role.setName("ADMIN");

        Role other = new Role();
        other.setId(1);
        other.setName("USER");

        assertNotEquals(role, other);
    }

    @Test
    @DisplayName("Test equals with null")
    void testEqualsWithNull() {
        role.setId(1);
        assertNotEquals(null, role);
    }

    @Test
    @DisplayName("Test equals with different class")
    void testEqualsWithDifferentClass() {
        role.setId(1);
        assertNotEquals("String object", role);
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        role.setId(1);
        role.setName("ADMIN");

        int hashCode1 = role.hashCode();
        int hashCode2 = role.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test hashCode with equal objects")
    void testHashCodeWithEqualObjects() {
        role.setId(1);
        role.setName("ADMIN");

        Role other = new Role();
        other.setId(1);
        other.setName("ADMIN");

        assertEquals(role.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test hashCode with different objects")
    void testHashCodeWithDifferentObjects() {
        role.setId(1);
        role.setName("ADMIN");

        Role other = new Role();
        other.setId(2);
        other.setName("USER");

        assertNotEquals(role.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test toString contains field values")
    void testToString() {
        role.setId(1);
        role.setName("ADMIN");

        String toString = role.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("ADMIN"));
    }

    @Test
    @DisplayName("Test toString with null name")
    void testToStringWithNullName() {
        role.setId(1);
        role.setName(null);

        String toString = role.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("null"));
    }

    @Test
    @DisplayName("Test toString with default values")
    void testToStringWithDefaultValues() {
        String toString = role.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("0"));
    }

    @Test
    @DisplayName("Test multiple updates to same role")
    void testMultipleUpdates() {
        role.setId(1);
        role.setName("USER");

        assertEquals(1, role.getId());
        assertEquals("USER", role.getName());

        role.setName("ADMIN");
        assertEquals("ADMIN", role.getName());

        role.setId(2);
        assertEquals(2, role.getId());
    }

    @Test
    @DisplayName("Test role with common role names")
    void testRoleWithCommonNames() {
        String[] commonRoles = {"ADMIN", "USER", "MANAGER", "GUEST", "MODERATOR", "EDITOR"};

        for (String roleName : commonRoles) {
            role.setName(roleName);
            assertEquals(roleName, role.getName());
        }
    }

    @Test
    @DisplayName("Test role hierarchy by id")
    void testRoleHierarchyById() {
        role.setId(1);
        role.setName("ADMIN");
        assertEquals(1, role.getId());

        Role userRole = new Role();
        userRole.setId(2);
        userRole.setName("USER");

        assertTrue(role.getId() < userRole.getId());
    }

    @Test
    @DisplayName("Test role state after multiple changes")
    void testRoleStateAfterMultipleChanges() {
        role.setId(1);
        role.setName("Name1");

        role.setId(2);
        role.setName("Name2");

        role.setId(3);
        role.setName("Name3");

        assertEquals(3, role.getId());
        assertEquals("Name3", role.getName());
    }

    @Test
    @DisplayName("Test role with unicode characters in name")
    void testRoleWithUnicodeInName() {
        String name = "ADMINISTRАTOR";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test role with numeric suffix")
    void testRoleWithNumericSuffix() {
        String name = "ADMIN_LVL_5";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    @DisplayName("Test role id boundaries")
    void testRoleIdBoundaries() {
        role.setId(0);
        assertEquals(0, role.getId());

        role.setId(1);
        assertEquals(1, role.getId());

        role.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, role.getId());
    }

    @Test
    @DisplayName("Test role complete lifecycle")
    void testRoleCompleteLifecycle() {
        assertEquals(0, role.getId());
        assertNull(role.getName());

        role.setId(1);
        role.setName("NEW_ROLE");

        assertEquals(1, role.getId());
        assertEquals("NEW_ROLE", role.getName());

        role.setName("UPDATED_ROLE");
        assertEquals("UPDATED_ROLE", role.getName());
    }

    @Test
    @DisplayName("Test role uniqueness constraint simulation")
    void testRoleUniquenessSimulation() {
        role.setId(1);
        role.setName("UNIQUE_ROLE");

        Role anotherRole = new Role();
        anotherRole.setId(2);
        anotherRole.setName("UNIQUE_ROLE");

        assertNotEquals(role.getId(), anotherRole.getId());
        assertEquals(role.getName(), anotherRole.getName());
    }

    @Test
    @DisplayName("Test role name with maximum realistic length")
    void testRoleNameMaximumRealisticLength() {
        String name = "SUPER_ADMINISTRATOR_WITH_FULL_SYSTEM_ACCESS";
        role.setName(name);
        assertEquals(name, role.getName());
        assertTrue(role.getName().length() > 20);
    }

    @Test
    @DisplayName("Test role default id value")
    void testRoleDefaultIdValue() {
        Role newRole = new Role();
        assertEquals(0, newRole.getId());
    }

    @Test
    @DisplayName("Test role with sequential id assignment")
    void testRoleSequentialIdAssignment() {
        for (int i = 1; i <= 10; i++) {
            role.setId(i);
            assertEquals(i, role.getId());
        }
    }

    @Test
    @DisplayName("Test role name case sensitivity")
    void testRoleNameCaseSensitivity() {
        role.setName("Admin");
        assertEquals("Admin", role.getName());

        role.setName("admin");
        assertEquals("admin", role.getName());

        role.setName("ADMIN");
        assertEquals("ADMIN", role.getName());
    }
}
