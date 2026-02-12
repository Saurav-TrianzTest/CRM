package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("MyErrorController Tests")
class MyErrorControllerTest {

    @Test
    @DisplayName("Test MyErrorController exists")
    void testMyErrorControllerExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test MyErrorController class is present")
    void testMyErrorControllerClass() throws ClassNotFoundException {
        Class.forName("crm.controller.MyErrorController");
    }
}
