package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateTimeTestControllerTest {

    private DateTimeTestController controller;
    private Model model;

    @BeforeEach
    void setUp() {
        controller = new DateTimeTestController();
        model = mock(Model.class);
    }

    @Test
    void constructor_shouldCreateController() {
        // Assert
        assertNotNull(controller);
    }

    @Test
    void dateTimeTest_shouldReturnViewName() {
        // Act
        String viewName = controller.dateTimeTest(model);
        
        // Assert
        assertEquals("date/test", viewName);
    }

    @Test
    void dateTimeTest_shouldAddAttributesToModel() {
        // Act
        controller.dateTimeTest(model);
        
        // Assert
        verify(model, times(4)).addAttribute(anyString(), any());
    }

    @Test
    void controller_shouldHaveControllerAnnotation() {
        // Assert
        assertTrue(DateTimeTestController.class.isAnnotationPresent(
            org.springframework.stereotype.Controller.class));
    }

    @Test
    void controller_shouldHaveRequestMappingAnnotation() {
        // Assert
        assertTrue(DateTimeTestController.class.isAnnotationPresent(
            org.springframework.web.bind.annotation.RequestMapping.class));
    }
}
