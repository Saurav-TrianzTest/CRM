package crm.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {

    @Test
    void roleService_shouldBeInterface() {
        // Assert
        assertTrue(RoleService.class.isInterface());
    }

    @Test
    void listAllRoles_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            RoleService.class.getMethod("listAllRoles");
        });
    }

    @Test
    void listAllRoles_shouldReturnIterable() throws NoSuchMethodException {
        // Arrange
        var method = RoleService.class.getMethod("listAllRoles");
        
        // Assert
        assertEquals(Iterable.class, method.getReturnType());
    }
}
