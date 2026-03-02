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
    void testSetAndGetId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    void testSetAndGetName() {
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(role);
        assertEquals(0, role.getId());
        assertNull(role.getName());
    }

    @Test
    void testSetNameWithNull() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    void testSetNameWithEmptyString() {
        role.setName("");
        assertEquals("", role.getName());
    }

    @Test
    void testSetIdWithNegative() {
        role.setId(-1);
        assertEquals(-1, role.getId());
    }

    @Test
    void testSetIdWithZero() {
        role.setId(0);
        assertEquals(0, role.getId());
    }

    @Test
    void testMultipleRoleObjects() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");

        assertNotEquals(role1.getId(), role2.getId());
        assertNotEquals(role1.getName(), role2.getName());
    }
}
