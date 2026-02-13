package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;

    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }

    @Test
    void testConstructor() {
        RegisterController controller = new RegisterController(userService);
        assert controller != null;
    }

    @Test
    void testShowRegistrationPage_Success() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testShowRegistrationPage_WithUserAttribute() throws Exception {
        mockMvc.perform(get("/register")
                        .flashAttr("user", testUser))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testProcessRegistrationForm_Success() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).saveUser(any(User.class));

        String result = registerController.processRegistrationForm(null, testUser, bindingResult);

        assert result.equals("success");
        verify(userService, times(1)).findByUsername("testuser");
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void testProcessRegistrationForm_UserAlreadyExists() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("testuser");

        when(userService.findByUsername("testuser")).thenReturn(existingUser);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("alreadyRegisteredMessage"));

        verify(userService, times(1)).findByUsername("testuser");
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void testProcessRegistrationForm_ValidationErrors() {
        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = registerController.processRegistrationForm(null, testUser, bindingResult);

        assert result.equals("redirect:/register");
        verify(userService, times(1)).findByUsername("testuser");
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void testProcessRegistrationForm_UserExistsAndValidationErrors() {
        User existingUser = new User();
        existingUser.setUsername("testuser");

        when(userService.findByUsername("testuser")).thenReturn(existingUser);
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = registerController.processRegistrationForm(null, testUser, bindingResult);

        assert result.equals("register");
        verify(userService, times(1)).findByUsername("testuser");
        verify(bindingResult, times(1)).reject("email");
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void testProcessRegistrationForm_EmptyUsername() throws Exception {
        when(userService.findByUsername("")).thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("username", "")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(1)).findByUsername("");
    }

    @Test
    void testProcessRegistrationForm_NullUsername() {
        testUser.setUsername(null);

        when(userService.findByUsername(null)).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).saveUser(any(User.class));

        String result = registerController.processRegistrationForm(null, testUser, bindingResult);

        assert result.equals("success");
        verify(userService, times(1)).findByUsername(null);
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void testProcessRegistrationForm_WithModel() throws Exception {
        when(userService.findByUsername("newuser")).thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("username", "newuser")
                        .param("email", "new@example.com")
                        .param("password", "newpassword"))
                .andExpect(status().isOk());

        verify(userService, times(1)).findByUsername("newuser");
    }

    @Test
    void testProcessRegistrationForm_MultipleRegistrations() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword("password" + i);

            when(userService.findByUsername("user" + i)).thenReturn(null);
            when(bindingResult.hasErrors()).thenReturn(false);
            doNothing().when(userService).saveUser(user);

            String result = registerController.processRegistrationForm(null, user, bindingResult);

            assert result.equals("success");
            verify(userService, times(1)).findByUsername("user" + i);
            verify(userService, times(1)).saveUser(user);
        }
    }

    @Test
    void testProcessRegistrationForm_SpecialCharactersInUsername() {
        testUser.setUsername("test@user#123");

        when(userService.findByUsername("test@user#123")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).saveUser(any(User.class));

        String result = registerController.processRegistrationForm(null, testUser, bindingResult);

        assert result.equals("success");
        verify(userService, times(1)).findByUsername("test@user#123");
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void testProcessRegistrationForm_LongUsername() {
        String longUsername = "a".repeat(255);
        testUser.setUsername(longUsername);

        when(userService.findByUsername(longUsername)).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).saveUser(any(User.class));

        String result = registerController.processRegistrationForm(null, testUser, bindingResult);

        assert result.equals("success");
        verify(userService, times(1)).findByUsername(longUsername);
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void testProcessRegistrationForm_ExistingUserWithModel() {
        User existingUser = new User();
        existingUser.setUsername("testuser");

        when(userService.findByUsername("testuser")).thenReturn(existingUser);
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = registerController.processRegistrationForm(null, testUser, bindingResult);

        assert result.equals("register");
        verify(bindingResult, times(1)).reject("email");
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void testShowRegistrationPage_MultipleRequests() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/register"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("register"))
                    .andExpect(model().attributeExists("user"));
        }
    }

    @Test
    void testProcessRegistrationForm_WithAllFields() throws Exception {
        when(userService.findByUsername("completeuser")).thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("username", "completeuser")
                        .param("email", "complete@example.com")
                        .param("password", "ComplexP@ssw0rd!")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk());

        verify(userService, times(1)).findByUsername("completeuser");
    }

    @Test
    void testProcessRegistrationForm_CaseSensitiveUsername() {
        User user1 = new User();
        user1.setUsername("TestUser");
        User user2 = new User();
        user2.setUsername("testuser");

        when(userService.findByUsername("TestUser")).thenReturn(null);
        when(userService.findByUsername("testuser")).thenReturn(user2);
        when(bindingResult.hasErrors()).thenReturn(false);

        String result1 = registerController.processRegistrationForm(null, user1, bindingResult);
        assert result1.equals("success");

        when(bindingResult.hasErrors()).thenReturn(false);
        String result2 = registerController.processRegistrationForm(null, user2, bindingResult);
        assert result2.equals("register");

        verify(userService, times(1)).findByUsername("TestUser");
        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    void testProcessRegistrationForm_InvalidEmail() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        .param("email", "invalid-email")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(1)).findByUsername("testuser");
    }
}
