package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegisterController registerController;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    public void testShowRegistrationPage() {
        String view = registerController.showRegistrationPage(model, testUser);
        assertEquals("register", view);
        verify(model, times(1)).addAttribute("user", testUser);
    }

    @Test
    public void testProcessRegistrationFormUserExists() {
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        String view = registerController.processRegistrationForm(model, testUser, bindingResult);

        assertEquals("register", view);
        verify(model, times(1)).addAttribute(eq("alreadyRegisteredMessage"), anyString());
    }

    @Test
    public void testProcessRegistrationFormWithErrors() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = registerController.processRegistrationForm(model, testUser, bindingResult);

        assertEquals("redirect:/register", view);
    }

    @Test
    public void testProcessRegistrationFormSuccess() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = registerController.processRegistrationForm(model, testUser, bindingResult);

        assertEquals("success", view);
        verify(userService, times(1)).saveUser(testUser);
    }
}
