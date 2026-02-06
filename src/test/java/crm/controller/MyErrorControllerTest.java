package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class MyErrorControllerTest {

    private MyErrorController errorController;

    @BeforeEach
    public void setUp() {
        errorController = new MyErrorController();
    }

    @Test
    public void testErrorControllerConstructor() {
        assertNotNull(errorController);
    }

    @Test
    public void testErrorMethod() {
        String result = errorController.error();
        assertNotNull(result);
        assertEquals("Error handling", result);
    }

    @Test
    public void testErrorMethodReturnsString() {
        String result = errorController.error();
        assertTrue(result instanceof String);
    }

    @Test
    public void testErrorMethodNotEmpty() {
        String result = errorController.error();
        assertFalse(result.isEmpty());
    }
}
