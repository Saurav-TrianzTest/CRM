package crm.controller;

import crm.entity.Role;
import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Test showAllUsers returns user list view")
    void testShowAllUsers() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("currentUser"));

        verify(userService, times(1)).findByUsername("testuser");
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    @WithMockUser
    @DisplayName("Test showFormEditUser returns edit view")
    void testShowFormEditUser() throws Exception {
        when(userService.showUser(1L)).thenReturn(user);

        mockMvc.perform(get("/user/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).showUser(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("Test processRequestEditUser with valid data redirects to list")
    void testProcessRequestEditUserSuccess() throws Exception {
        doNothing().when(userService).editUser(any(User.class));

        mockMvc.perform(post("/user/edit/1")
                        .param("id", "1")
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test processRequestEditUser with invalid data redirects to edit form")
    void testProcessRequestEditUserWithErrors() throws Exception {
        mockMvc.perform(post("/user/edit/1")
                        .param("id", "1")
                        .param("username", "")
                        .param("email", "invalid-email")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    @DisplayName("Test deleteUser deletes user and redirects to list")
    void testDeleteUser() throws Exception {
        when(userService.showUser(1L)).thenReturn(user);
        doNothing().when(userService).deleteUser(user);

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).showUser(1L);
        verify(userService, times(1)).deleteUser(user);
    }

    @Test
    @WithMockUser
    @DisplayName("Test showFormEditUser with different user id")
    void testShowFormEditUserDifferentId() throws Exception {
        User user2 = User.builder().id(2L).username("user2").build();
        when(userService.showUser(2L)).thenReturn(user2);

        mockMvc.perform(get("/user/edit/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"));

        verify(userService, times(1)).showUser(2L);
    }

    @Test
    @WithMockUser
    @DisplayName("Test deleteUser with different user id")
    void testDeleteUserDifferentId() throws Exception {
        User user2 = User.builder().id(5L).username("user5").build();
        when(userService.showUser(5L)).thenReturn(user2);

        mockMvc.perform(get("/user/delete/5"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(1)).showUser(5L);
        verify(userService, times(1)).deleteUser(user2);
    }

    @Test
    @WithMockUser
    @DisplayName("Test user list endpoint is mapped correctly")
    void testUserListEndpointMapping() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test edit endpoint requires valid user id")
    void testEditEndpointWithValidId() throws Exception {
        when(userService.showUser(1L)).thenReturn(user);

        mockMvc.perform(get("/user/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user));
    }

    @Test
    @WithMockUser
    @DisplayName("Test POST to edit requires CSRF token")
    void testEditPostRequiresCsrf() throws Exception {
        mockMvc.perform(post("/user/edit/1")
                        .param("username", "testuser"))
                .andExpect(status().isForbidden());
    }
}
