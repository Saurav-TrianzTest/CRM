package crm.service;

import crm.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void userService_shouldBeInterface() {
        // Assert
        assertTrue(UserService.class.isInterface());
    }

    @Test
    void findByUsername_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserService.class.getMethod("findByUsername", String.class);
        });
    }

    @Test
    void listAllUsers_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserService.class.getMethod("listAllUsers");
        });
    }

    @Test
    void showUser_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserService.class.getMethod("showUser", Long.class);
        });
    }

    @Test
    void saveUser_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserService.class.getMethod("saveUser", User.class);
        });
    }

    @Test
    void editUser_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserService.class.getMethod("editUser", User.class);
        });
    }

    @Test
    void deleteUser_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserService.class.getMethod("deleteUser", User.class);
        });
    }

    @Test
    void findByUsername_shouldReturnUser() throws NoSuchMethodException {
        // Arrange
        var method = UserService.class.getMethod("findByUsername", String.class);
        
        // Assert
        assertEquals(User.class, method.getReturnType());
    }

    @Test
    void listAllUsers_shouldReturnIterable() throws NoSuchMethodException {
        // Arrange
        var method = UserService.class.getMethod("listAllUsers");
        
        // Assert
        assertEquals(Iterable.class, method.getReturnType());
    }
}
