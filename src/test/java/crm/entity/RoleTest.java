package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testRoleCreation() {
        assertNotNull(role);
    }

    @Test
    void testSetAndGetId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    void testSetAndGetName() {
        role.setName("ADMIN");
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void testRoleWithNullName() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    void testRoleWithEmptyName() {
        role.setName("");
        assertEquals("", role.getName());
    }

    @Test
    void testRoleWithDifferentIds() {
        role.setId(10);
        assertEquals(10, role.getId());

        role.setId(100);
        assertEquals(100, role.getId());
    }

    @Test
    void testRoleWithMultipleNames() {
        role.setName("USER");
        assertEquals("USER", role.getName());

        role.setName("MANAGER");
        assertEquals("MANAGER", role.getName());
    }

    @Test
    void testRoleEquality() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ADMIN");

        assertEquals(role1, role2);
    }

    @Test
    void testRoleHashCode() {
        role.setId(1);
        role.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ADMIN");

        assertEquals(role.hashCode(), role2.hashCode());
    }

    @Test
    void testRoleToString() {
        role.setId(1);
        role.setName("ADMIN");

        String result = role.toString();
        assertNotNull(result);
        assertTrue(result.contains("ADMIN"));
    }
}
