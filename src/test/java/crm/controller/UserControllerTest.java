package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();
    }

    @Test
    public void testShowAllUsers() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());

        String view = userController.showAllUsers(model, userDetails);

        assertEquals("user/list", view);
        verify(model, times(1)).addAttribute(eq("currentUser"), any());
        verify(model, times(1)).addAttribute(eq("users"), any());
    }

    @Test
    public void testShowFormEditUser() {
        when(userService.showUser(1L)).thenReturn(testUser);

        String view = userController.showFormEditUser(model, 1L);

        assertEquals("user/edit", view);
        verify(model, times(1)).addAttribute("user", testUser);
    }

    @Test
    public void testProcessRequestEditUserWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = userController.processRequestEditUser(1L, testUser, bindingResult);

        assertEquals("redirect:/user/edit/1", view);
    }

    @Test
    public void testProcessRequestEditUserSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = userController.processRequestEditUser(1L, testUser, bindingResult);

        assertEquals("redirect:/user/list", view);
        verify(userService, times(1)).editUser(testUser);
    }

    @Test
    public void testDeleteUser() {
        when(userService.showUser(1L)).thenReturn(testUser);

        String view = userController.deleteUser(1L);

        assertEquals("redirect:/user/list", view);
        verify(userService, times(1)).deleteUser(testUser);
    }
}
