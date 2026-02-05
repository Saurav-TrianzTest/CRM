package crm.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void testUserServiceInterfaceExists() {
        assertDoesNotThrow(() -> {
            Class.forName("crm.service.UserService");
        });
    }

    @Test
    public void testUserServiceHasFindByUsernameMethod() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("findByUsername", String.class));
    }

    @Test
    public void testUserServiceHasListAllUsersMethod() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("listAllUsers"));
    }

    @Test
    public void testUserServiceHasShowUserMethod() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("showUser", Long.class));
    }

    @Test
    public void testUserServiceHasSaveUserMethod() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("saveUser", crm.entity.User.class));
    }

    @Test
    public void testUserServiceHasEditUserMethod() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("editUser", crm.entity.User.class));
    }

    @Test
    public void testUserServiceHasDeleteUserMethod() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("deleteUser", crm.entity.User.class));
    }
}
