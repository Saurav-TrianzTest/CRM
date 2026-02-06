package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private Export export;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDownload() {
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().id(1L).username("testuser").build());

        when(userService.listAllUsers()).thenReturn(users);

        String result = export.download(model);

        verify(userService, times(1)).listAllUsers();
        verify(model, times(1)).addAttribute("users", users);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void testDownloadWithEmptyUsers() {
        ArrayList<User> users = new ArrayList<>();

        when(userService.listAllUsers()).thenReturn(users);

        String result = export.download(model);

        verify(userService, times(1)).listAllUsers();
        assertNotNull(result);
    }

    @Test
    void testDownloadReturnsEmptyString() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        String result = export.download(model);
        assertEquals("", result);
    }
}
