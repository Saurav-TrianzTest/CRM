package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    public void testShowAllUsers() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());

        String result = userController.showAllUsers(model, userDetails);
        assertEquals("user/list", result);
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    public void testShowFormEditUser() {
        when(userService.showUser(1L)).thenReturn(user);
        String result = userController.showFormEditUser(model, 1L);
        assertEquals("user/edit", result);
        verify(userService, times(1)).showUser(1L);
    }

    @Test
    public void testProcessRequestEditUserWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = userController.processRequestEditUser(1L, user, bindingResult);
        assertEquals("redirect:/user/edit/1", result);
    }

    @Test
    public void testProcessRequestEditUserSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = userController.processRequestEditUser(1L, user, bindingResult);
        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).editUser(user);
    }

    @Test
    public void testDeleteUser() {
        when(userService.showUser(1L)).thenReturn(user);
        String result = userController.deleteUser(1L);
        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).deleteUser(user);
    }

    @Test
    public void testUserControllerConstructor() {
        assertNotNull(userController);
    }

    @Test
    public void testShowAllUsersAddsModelAttributes() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        userController.showAllUsers(model, userDetails);
        verify(model, times(1)).addAttribute(eq("currentUser"), any());
        verify(model, times(1)).addAttribute(eq("users"), any());
    }
}
