package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportTest {

    private Export export;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        export = new Export(userService);
    }

    @Test
    void testConstructor() {
        assertNotNull(export);
    }

    @Test
    void testDownload() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername("testuser");
        users.add(user);

        when(userService.listAllUsers()).thenReturn(users);

        String result = export.download(model);

        assertEquals("", result);
        verify(userService, times(1)).listAllUsers();
        verify(model, times(1)).addAttribute("users", users);
    }

    @Test
    void testDownloadWithEmptyList() {
        List<User> users = new ArrayList<>();
        when(userService.listAllUsers()).thenReturn(users);

        String result = export.download(model);

        assertEquals("", result);
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownloadModelAttribute() {
        List<User> users = new ArrayList<>();
        when(userService.listAllUsers()).thenReturn(users);

        export.download(model);

        verify(model).addAttribute(eq("users"), any());
    }
}
