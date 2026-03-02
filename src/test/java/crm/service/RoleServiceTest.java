package crm.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {

    @Test
    void testRoleServiceInterface() {
        assertNotNull(RoleService.class);
    }

    @Test
    void testListAllRolesMethodExists() throws NoSuchMethodException {
        RoleService.class.getMethod("listAllRoles");
    }
}
