package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class MyErrorControllerTest {

    private MyErrorController myErrorController;

    @BeforeEach
    public void setUp() {
        myErrorController = new MyErrorController();
    }

    @Test
    public void testMyErrorControllerCreation() {
        assertNotNull(myErrorController);
    }

    @Test
    public void testErrorMethod() {
        String result = myErrorController.error();
        assertNotNull(result);
        assertEquals("Error handling", result);
    }

    @Test
    public void testErrorMethodReturnsString() {
        String result = myErrorController.error();
        assertTrue(result instanceof String);
    }

    @Test
    public void testErrorMethodNotNull() {
        String result = myErrorController.error();
        assertNotNull(result);
    }

    @Test
    public void testErrorMethodNotEmpty() {
        String result = myErrorController.error();
        assertFalse(result.isEmpty());
    }
}
