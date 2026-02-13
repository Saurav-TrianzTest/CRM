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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private Export exportController;

    private MockMvc mockMvc;

    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(exportController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        User user3 = new User();
        user3.setId(3L);
        user3.setUsername("user3");
        user3.setEmail("user3@example.com");

        testUsers = Arrays.asList(testUser, user2, user3);
    }

    @Test
    void testConstructor() {
        Export controller = new Export(userService);
        assert controller != null;
    }

    @Test
    void testDownload_Success() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", testUsers));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_EmptyUserList() throws Exception {
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_SingleUser() throws Exception {
        when(userService.listAllUsers()).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", Arrays.asList(testUser)));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_NullUserList() throws Exception {
        when(userService.listAllUsers()).thenReturn(null);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", org.hamcrest.Matchers.nullValue()));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_LargeUserList() throws Exception {
        User[] users = new User[100];
        for (int i = 0; i < 100; i++) {
            users[i] = new User();
            users[i].setId((long) i);
            users[i].setUsername("user" + i);
            users[i].setEmail("user" + i + "@example.com");
        }
        List<User> largeUserList = Arrays.asList(users);

        when(userService.listAllUsers()).thenReturn(largeUserList);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", largeUserList));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_MultipleRequests() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/download"))
                    .andExpect(status().isOk())
                    .andExpect(view().name(""))
                    .andExpect(model().attributeExists("users"));
        }

        verify(userService, times(5)).listAllUsers();
    }

    @Test
    void testDownload_ViewNameIsEmpty() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_ModelContainsUsers() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().size(1));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_UsersAttributeNotNull() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", org.hamcrest.Matchers.notNullValue()));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_WithHeaders() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download")
                        .header("User-Agent", "Test Agent")
                        .header("Accept", "application/vnd.ms-excel"))
                .andExpect(status().isOk())
                .andExpect(view().name(""));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_WithAcceptHeader() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download")
                        .header("Accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(status().isOk())
                .andExpect(view().name(""));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_ConsecutiveCalls() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk());

        verify(userService, times(3)).listAllUsers();
    }

    @Test
    void testDownload_UsersListType() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users",
                    org.hamcrest.Matchers.instanceOf(List.class)));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_VerifyUserServiceCalled() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk());

        verify(userService, times(1)).listAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testDownload_WithDifferentUserCounts() throws Exception {
        int[] counts = {0, 1, 5, 10, 50, 100};

        for (int count : counts) {
            User[] users = new User[count];
            for (int i = 0; i < count; i++) {
                users[i] = new User();
                users[i].setId((long) i);
                users[i].setUsername("user" + i);
            }
            List<User> userList = Arrays.asList(users);

            when(userService.listAllUsers()).thenReturn(userList);

            mockMvc.perform(get("/download"))
                    .andExpect(status().isOk())
                    .andExpect(view().name(""))
                    .andExpect(model().attributeExists("users"));

            reset(userService);
        }
    }

    @Test
    void testDownload_RapidSuccessiveCalls() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/download"))
                    .andExpect(status().isOk())
                    .andExpect(view().name(""));
        }

        verify(userService, times(10)).listAllUsers();
    }

    @Test
    void testDownload_WithQueryParameters() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download")
                        .param("format", "xlsx")
                        .param("type", "excel"))
                .andExpect(status().isOk())
                .andExpect(view().name(""));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_WithSpecialCharactersInUserData() throws Exception {
        User specialUser = new User();
        specialUser.setId(1L);
        specialUser.setUsername("user@special#123");
        specialUser.setEmail("test+email@example.com");

        when(userService.listAllUsers()).thenReturn(Arrays.asList(specialUser));

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_WithLongUsername() throws Exception {
        User longUsernameUser = new User();
        longUsernameUser.setId(1L);
        longUsernameUser.setUsername("a".repeat(255));

        when(userService.listAllUsers()).thenReturn(Arrays.asList(longUsernameUser));

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_HttpStatusOk() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download"))
                .andExpect(status().isOk());

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_WithCharacterEncoding() throws Exception {
        when(userService.listAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/download")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(view().name(""));

        verify(userService, times(1)).listAllUsers();
    }
}
