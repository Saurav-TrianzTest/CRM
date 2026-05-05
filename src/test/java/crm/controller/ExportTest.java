package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportTest {

    private Export export;
    private UserService userService;
    private Model model;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        export = new Export(userService);
        model = mock(Model.class);
    }

    @Test
    void constructor_shouldCreateExport() {
        // Assert
        assertNotNull(export);
    }

    @Test
    void download_shouldReturnEmptyString() {
        // Act
        String result = export.download(model);
        
        // Assert
        assertEquals("", result);
    }

    @Test
    void download_shouldAddUsersToModel() {
        // Act
        export.download(model);
        
        // Assert
        verify(model).addAttribute(eq("users"), any());
        verify(userService).listAllUsers();
    }

    @Test
    void controller_shouldHaveControllerAnnotation() {
        // Assert
        assertTrue(Export.class.isAnnotationPresent(
            org.springframework.stereotype.Controller.class));
    }
}
