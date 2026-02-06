package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

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
    void setUp() {
        user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void testShowRegistrationPage() {
        String viewName = registerController.showRegistrationPage(model, user);

        assertEquals("register", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testProcessRegistrationFormSuccess() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = registerController.processRegistrationForm(model, user, bindingResult);

        assertEquals("success", viewName);
        verify(userService).saveUser(user);
    }

    @Test
    void testProcessRegistrationFormUserExists() {
        User existingUser = User.builder().username("testuser").build();
        when(userService.findByUsername("testuser")).thenReturn(existingUser);

        String viewName = registerController.processRegistrationForm(model, user, bindingResult);

        assertEquals("register", viewName);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
        verify(bindingResult).reject("email");
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testProcessRegistrationFormWithErrors() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = registerController.processRegistrationForm(model, user, bindingResult);

        assertEquals("redirect:/register", viewName);
        verify(userService, never()).saveUser(any());
    }
}
