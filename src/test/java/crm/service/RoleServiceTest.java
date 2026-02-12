package crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RoleService Interface Tests")
class RoleServiceTest {

    @Test
    @DisplayName("Test RoleService is an interface")
    void testRoleServiceIsInterface() {
        assertTrue(RoleService.class.isInterface());
    }

    @Test
    @DisplayName("Test RoleService has methods")
    void testRoleServiceHasMethods() {
        int methodCount = RoleService.class.getDeclaredMethods().length;
        assertTrue(methodCount > 0);
    }

    @Test
    @DisplayName("Test RoleService is public")
    void testRoleServiceIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(RoleService.class.getModifiers()));
    }
}
