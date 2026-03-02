package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController controller;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private UserDetails userDetails;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserController(userService);
    }

    @Test
    void testShowAllUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername("testuser");
        users.add(user);

        when(userDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(users);

        String result = controller.showAllUsers(model, userDetails);

        assertEquals("user/list", result);
        verify(model).addAttribute("currentUser", user);
        verify(model).addAttribute("users", users);
    }

    @Test
    void testShowFormEditUser() {
        User user = new User();
        user.setId(1L);

        when(userService.showUser(1L)).thenReturn(user);

        String result = controller.showFormEditUser(model, 1L);

        assertEquals("user/edit", result);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testProcessRequestEditUserSuccess() {
        User user = new User();
        user.setId(1L);

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = controller.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/list", result);
        verify(userService).editUser(user);
    }

    @Test
    void testProcessRequestEditUserWithErrors() {
        User user = new User();
        user.setId(1L);

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = controller.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/edit/1", result);
        verify(userService, never()).editUser(user);
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1L);

        when(userService.showUser(1L)).thenReturn(user);

        String result = controller.deleteUser(1L);

        assertEquals("redirect:/user/list", result);
        verify(userService).deleteUser(user);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }
}
