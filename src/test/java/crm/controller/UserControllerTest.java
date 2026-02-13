package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        testUsers = Arrays.asList(testUser, user2);
    }

    @Test
    void testConstructor() {
        UserController controller = new UserController(userService);
        assert controller != null;
    }

    @Test
    void testShowAllUsers_Success() throws Exception {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/user/list")
                        .principal(() -> "testuser")
                        .sessionAttr("userDetails", userDetails))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("currentUser"));

        verify(userService, times(1)).findByUsername("testuser");
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testShowAllUsers_EmptyList() throws Exception {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/user/list")
                        .principal(() -> "testuser")
                        .sessionAttr("userDetails", userDetails))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("currentUser"));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testShowFormEditUser_Success() throws Exception {
        when(userService.showUser(1L)).thenReturn(testUser);

        mockMvc.perform(get("/user/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", testUser));

        verify(userService, times(1)).showUser(1L);
    }

    @Test
    void testShowFormEditUser_NullUser() throws Exception {
        when(userService.showUser(999L)).thenReturn(null);

        mockMvc.perform(get("/user/edit/{id}", 999L))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).showUser(999L);
    }

    @Test
    void testProcessRequestEditUser_Success() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).editUser(any(User.class));

        mockMvc.perform(post("/user/edit/{id}", 1L)
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).editUser(any(User.class));
    }

    @Test
    void testProcessRequestEditUser_ValidationErrors() throws Exception {
        mockMvc.perform(post("/user/edit/{id}", 1L)
                        .param("username", "")
                        .param("email", "invalid-email")
                        .param("password", ""))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testProcessRequestEditUser_WithBindingErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = userController.processRequestEditUser(1L, testUser, bindingResult);

        assert result.equals("redirect:/user/edit/1");
        verify(userService, never()).editUser(any(User.class));
    }

    @Test
    void testProcessRequestEditUser_WithoutBindingErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).editUser(any(User.class));

        String result = userController.processRequestEditUser(1L, testUser, bindingResult);

        assert result.equals("redirect:/user/list");
        verify(userService, times(1)).editUser(testUser);
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        when(userService.showUser(1L)).thenReturn(testUser);
        doNothing().when(userService).deleteUser(testUser);

        mockMvc.perform(get("/user/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).showUser(1L);
        verify(userService, times(1)).deleteUser(testUser);
    }

    @Test
    void testDeleteUser_NonExistentUser() throws Exception {
        when(userService.showUser(999L)).thenReturn(null);
        doNothing().when(userService).deleteUser(null);

        mockMvc.perform(get("/user/delete/{id}", 999L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).showUser(999L);
        verify(userService, times(1)).deleteUser(null);
    }

    @Test
    void testDeleteUser_MultipleUsers() throws Exception {
        for (int i = 1; i <= 3; i++) {
            User user = new User();
            user.setId((long) i);
            when(userService.showUser((long) i)).thenReturn(user);
            doNothing().when(userService).deleteUser(user);

            mockMvc.perform(get("/user/delete/{id}", i))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/user/list"));
        }

        verify(userService, times(3)).deleteUser(any(User.class));
    }

    @Test
    void testShowAllUsers_WithDifferentUsernames() throws Exception {
        String[] usernames = {"user1", "user2", "admin", "testuser"};

        for (String username : usernames) {
            when(userDetails.getUsername()).thenReturn(username);
            when(userService.findByUsername(username)).thenReturn(testUser);
            when(userService.listAllUsers()).thenReturn(testUsers);

            mockMvc.perform(get("/user/list")
                            .principal(() -> username)
                            .sessionAttr("userDetails", userDetails))
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/list"));

            verify(userService, times(1)).findByUsername(username);
        }
    }

    @Test
    void testEditUser_WithSpecialCharacters() throws Exception {
        when(userService.showUser(1L)).thenReturn(testUser);

        mockMvc.perform(get("/user/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"));

        verify(userService, times(1)).showUser(1L);
    }

    @Test
    void testProcessRequestEditUser_WithLargeId() throws Exception {
        Long largeId = Long.MAX_VALUE;

        mockMvc.perform(post("/user/edit/{id}", largeId)
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testDeleteUser_WithZeroId() throws Exception {
        when(userService.showUser(0L)).thenReturn(null);
        doNothing().when(userService).deleteUser(null);

        mockMvc.perform(get("/user/delete/{id}", 0L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).showUser(0L);
    }

    @Test
    void testShowFormEditUser_MultipleRequests() throws Exception {
        for (int i = 0; i < 5; i++) {
            when(userService.showUser(1L)).thenReturn(testUser);

            mockMvc.perform(get("/user/edit/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/edit"));
        }

        verify(userService, times(5)).showUser(1L);
    }
}
