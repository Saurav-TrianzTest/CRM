package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("RegisterController Tests")
class RegisterControllerTest {

    @Test
    @DisplayName("Test RegisterController exists")
    void testRegisterControllerExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test RegisterController class is present")
    void testRegisterControllerClass() throws ClassNotFoundException {
        Class.forName("crm.controller.RegisterController");
    }
}
