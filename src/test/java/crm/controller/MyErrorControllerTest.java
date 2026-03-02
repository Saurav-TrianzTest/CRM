package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyErrorControllerTest {

    private MyErrorController controller;

    @BeforeEach
    void setUp() {
        controller = new MyErrorController();
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    void testError() {
        String result = controller.error();
        assertEquals("Error handling", result);
        assertNotNull(result);
    }

    @Test
    void testErrorNotNull() {
        String errorMessage = controller.error();
        assertNotNull(errorMessage);
    }

    @Test
    void testErrorReturnString() {
        String errorMessage = controller.error();
        assertTrue(errorMessage instanceof String);
    }
}
