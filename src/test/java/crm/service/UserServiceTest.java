package crm.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void testUserServiceInterface() {
        assertNotNull(UserService.class);
    }

    @Test
    void testFindByUsernameMethodExists() throws NoSuchMethodException {
        UserService.class.getMethod("findByUsername", String.class);
    }

    @Test
    void testListAllUsersMethodExists() throws NoSuchMethodException {
        UserService.class.getMethod("listAllUsers");
    }

    @Test
    void testShowUserMethodExists() throws NoSuchMethodException {
        UserService.class.getMethod("showUser", Long.class);
    }
}
