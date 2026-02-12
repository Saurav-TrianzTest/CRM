package crm.service;

import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserService Interface Tests")
class UserServiceTest {

    @Test
    @DisplayName("Test UserService is an interface")
    void testUserServiceIsInterface() {
        assertTrue(UserService.class.isInterface());
    }

    @Test
    @DisplayName("Test UserService has correct method signatures")
    void testUserServiceMethodSignatures() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("findByUsername", String.class));
        assertNotNull(UserService.class.getMethod("listAllUsers"));
        assertNotNull(UserService.class.getMethod("showUser", Long.class));
        assertNotNull(UserService.class.getMethod("saveUser", User.class));
        assertNotNull(UserService.class.getMethod("editUser", User.class));
        assertNotNull(UserService.class.getMethod("deleteUser", User.class));
    }

    @Test
    @DisplayName("Test UserService methods count")
    void testUserServiceMethodsCount() {
        int methodCount = UserService.class.getDeclaredMethods().length;
        assertEquals(6, methodCount);
    }
}
