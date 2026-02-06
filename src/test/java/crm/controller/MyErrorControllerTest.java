package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class MyErrorControllerTest {

    @Test
    void testControllerClassExists() {
        assertNotNull(MyErrorController.class);
    }

    @Test
    void testControllerInstantiation() {
        MyErrorController controller = new MyErrorController();
        assertNotNull(controller);
    }
}
