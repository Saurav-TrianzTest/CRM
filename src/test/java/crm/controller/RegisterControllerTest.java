package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterControllerTest {

    private RegisterController registerController;
    private UserService userService;
    private Model model;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        registerController = new RegisterController(userService);
        model = mock(Model.class);
        bindingResult = mock(BindingResult.class);
    }

    @Test
    void constructor_shouldCreateRegisterController() {
        // Assert
        assertNotNull(registerController);
    }

    @Test
    void showRegistrationPage_shouldReturnRegisterView() {
        // Arrange
        User user = new User();
        
        // Act
        String result = registerController.showRegistrationPage(model, user);
        
        // Assert
        assertEquals("register", result);
        verify(model).addAttribute("user", user);
    }

    @Test
    void processRegistrationForm_withNewUser_shouldReturnSuccess() {
        // Arrange
        User user = User.builder()
                .username("newuser")
                .email("new@example.com")
                .build();
        
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        
        // Act
        String result = registerController.processRegistrationForm(model, user, bindingResult);
        
        // Assert
        assertEquals("success", result);
        verify(userService).saveUser(user);
    }

    @Test
    void processRegistrationForm_withExistingUser_shouldReturnRegister() {
        // Arrange
        User existingUser = User.builder()
                .username("existing")
                .build();
        
        User newUser = User.builder()
                .username("existing")
                .build();
        
        when(userService.findByUsername("existing")).thenReturn(existingUser);
        
        // Act
        String result = registerController.processRegistrationForm(model, newUser, bindingResult);
        
        // Assert
        assertEquals("register", result);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
        verify(bindingResult).reject("email");
    }

    @Test
    void processRegistrationForm_withValidationErrors_shouldRedirectToRegister() {
        // Arrange
        User user = new User();
        
        when(userService.findByUsername(any())).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);
        
        // Act
        String result = registerController.processRegistrationForm(model, user, bindingResult);
        
        // Assert
        assertEquals("redirect:/register", result);
    }

    @Test
    void controller_shouldHaveControllerAnnotation() {
        // Assert
        assertTrue(RegisterController.class.isAnnotationPresent(
            org.springframework.stereotype.Controller.class));
    }
}
