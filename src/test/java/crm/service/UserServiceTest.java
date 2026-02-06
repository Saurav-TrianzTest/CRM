package crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Test
    void testInterfaceExists() {
        assertNotNull(UserService.class);
    }

    @Test
    void testInterfaceMethods() throws NoSuchMethodException {
        assertNotNull(UserService.class.getMethod("findByUsername", String.class));
        assertNotNull(UserService.class.getMethod("listAllUsers"));
        assertNotNull(UserService.class.getMethod("showUser", Long.class));
        assertNotNull(UserService.class.getMethod("saveUser", crm.entity.User.class));
        assertNotNull(UserService.class.getMethod("editUser", crm.entity.User.class));
        assertNotNull(UserService.class.getMethod("deleteUser", crm.entity.User.class));
    }
}
