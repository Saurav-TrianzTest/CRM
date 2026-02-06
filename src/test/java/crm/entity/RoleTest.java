package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(role);
    }

    @Test
    void testGettersAndSetters() {
        int id = 1;
        String name = "ROLE_ADMIN";

        role.setId(id);
        role.setName(name);

        assertEquals(id, role.getId());
        assertEquals(name, role.getName());
    }

    @Test
    void testSetId() {
        role.setId(5);

        assertEquals(5, role.getId());
    }

    @Test
    void testSetName() {
        role.setName("ROLE_USER");

        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testRoleAdmin() {
        role.setId(1);
        role.setName("ROLE_ADMIN");

        assertEquals(1, role.getId());
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void testRoleUser() {
        role.setId(2);
        role.setName("ROLE_USER");

        assertEquals(2, role.getId());
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testRoleManager() {
        role.setId(3);
        role.setName("ROLE_MANAGER");

        assertEquals(3, role.getId());
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testRoleOwner() {
        role.setId(4);
        role.setName("ROLE_OWNER");

        assertEquals(4, role.getId());
        assertEquals("ROLE_OWNER", role.getName());
    }

    @Test
    void testNullName() {
        role.setName(null);

        assertNull(role.getName());
    }

    @Test
    void testEmptyName() {
        role.setName("");

        assertEquals("", role.getName());
    }

    @Test
    void testEquality() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");

        assertEquals(role1, role2);
    }

    @Test
    void testHashCode() {
        role.setId(1);
        role.setName("ROLE_USER");

        int hashCode1 = role.hashCode();
        int hashCode2 = role.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testToString() {
        role.setId(1);
        role.setName("ROLE_ADMIN");

        String toString = role.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("ROLE_ADMIN"));
    }

    @Test
    void testNegativeId() {
        role.setId(-1);

        assertEquals(-1, role.getId());
    }

    @Test
    void testLargeId() {
        role.setId(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, role.getId());
    }
}
