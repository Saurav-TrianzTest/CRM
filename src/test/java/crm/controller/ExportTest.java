package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExportTest {

    private Export exportController;
    private UserService userService;
    private Model model;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        exportController = new Export(userService);
        model = mock(Model.class);
    }

    @Test
    public void testExportControllerCreation() {
        assertNotNull(exportController);
    }

    @Test
    public void testDownload() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        String result = exportController.download(model);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testDownloadAddsUsersToModel() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        exportController.download(model);
        verify(model, times(1)).addAttribute(eq("users"), any());
    }

    @Test
    public void testDownloadCallsUserService() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        exportController.download(model);
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    public void testDownloadReturnsEmptyString() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        String result = exportController.download(model);
        assertEquals("", result);
    }

    @Test
    public void testDownloadWithNullModel() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        assertThrows(NullPointerException.class, () -> exportController.download(null));
    }
}
