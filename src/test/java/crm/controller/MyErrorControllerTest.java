package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class MyErrorControllerTest {

    private MyErrorController myErrorController;

    @BeforeEach
    void setUp() {
        myErrorController = new MyErrorController();
    }

    @Test
    void constructor_shouldCreateMyErrorController() {
        // Assert
        assertNotNull(myErrorController);
    }

    @Test
    void error_shouldReturnErrorMessage() {
        // Act
        String result = myErrorController.error();
        
        // Assert
        assertNotNull(result);
        assertEquals("Error handling", result);
    }

    @Test
    void controller_shouldHaveRestControllerAnnotation() {
        // Assert
        assertTrue(MyErrorController.class.isAnnotationPresent(
            org.springframework.web.bind.annotation.RestController.class));
    }

    @Test
    void controller_shouldImplementErrorController() {
        // Assert
        assertTrue(org.springframework.boot.web.servlet.error.ErrorController.class
            .isAssignableFrom(MyErrorController.class));
    }
}
