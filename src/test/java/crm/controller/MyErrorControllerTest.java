package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyErrorControllerTest {

    private MyErrorController myErrorController;

    @BeforeEach
    void setUp() {
        myErrorController = new MyErrorController();
    }

    @Test
    void testError() {
        String result = myErrorController.error();
        assertNotNull(result);
        assertEquals("Error handling", result);
    }

    @Test
    void testErrorNotNull() {
        String result = myErrorController.error();
        assertNotNull(result);
    }

    @Test
    void testErrorReturnsExpectedMessage() {
        String result = myErrorController.error();
        assertTrue(result.contains("Error"));
    }
}
