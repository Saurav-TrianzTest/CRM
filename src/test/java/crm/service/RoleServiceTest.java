package crm.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest {

    @Test
    public void testRoleServiceInterfaceExists() {
        assertDoesNotThrow(() -> {
            Class.forName("crm.service.RoleService");
        });
    }

    @Test
    public void testRoleServiceHasListAllRolesMethod() throws NoSuchMethodException {
        assertNotNull(RoleService.class.getMethod("listAllRoles"));
    }
}
