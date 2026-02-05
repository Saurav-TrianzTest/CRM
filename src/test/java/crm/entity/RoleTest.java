package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
    }

    @Test
    public void testRoleCreation() {
        assertNotNull(role);
    }

    @Test
    public void testSetAndGetId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    public void testSetAndGetName() {
        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    public void testRoleWithNullName() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    public void testRoleWithEmptyName() {
        role.setName("");
        assertEquals("", role.getName());
    }

    @Test
    public void testRoleEquality() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_USER");

        assertEquals(role1, role2);
    }

    @Test
    public void testRoleHashCode() {
        role.setId(1);
        role.setName("ROLE_ADMIN");
        int hashCode = role.hashCode();
        assertTrue(hashCode != 0);
    }

    @Test
    public void testRoleToString() {
        role.setId(1);
        role.setName("ROLE_USER");
        String str = role.toString();
        assertNotNull(str);
        assertTrue(str.contains("ROLE_USER"));
    }
}
