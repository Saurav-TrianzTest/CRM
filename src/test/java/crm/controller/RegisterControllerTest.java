package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegisterController registerController;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    public void testShowRegistrationPage() {
        String result = registerController.showRegistrationPage(model, user);
        assertEquals("register", result);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    public void testProcessRegistrationFormUserExists() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        String result = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("register", result);
        verify(model, times(1)).addAttribute(eq("alreadyRegisteredMessage"), anyString());
    }

    @Test
    public void testProcessRegistrationFormWithErrors() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("redirect:/register", result);
    }

    @Test
    public void testProcessRegistrationFormSuccess() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("success", result);
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testRegisterControllerConstructor() {
        assertNotNull(registerController);
    }

    @Test
    public void testProcessRegistrationFormCallsUserService() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        registerController.processRegistrationForm(model, user, bindingResult);
        verify(userService, times(1)).findByUsername("testuser");
    }
}
