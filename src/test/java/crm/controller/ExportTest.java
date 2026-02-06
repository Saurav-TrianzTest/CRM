package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExportTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private Export export;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExportConstructor() {
        assertNotNull(export);
    }

    @Test
    public void testDownloadMethod() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        String result = export.download(model);
        assertNotNull(result);
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    public void testDownloadReturnsEmptyString() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        String result = export.download(model);
        assertEquals("", result);
    }

    @Test
    public void testDownloadCallsModelAddAttribute() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        export.download(model);
        verify(model, times(1)).addAttribute(eq("users"), any());
    }

    @Test
    public void testDownloadCallsUserService() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        export.download(model);
        verify(userService, times(1)).listAllUsers();
    }
}
