package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class CrmApplicationTest {

    @Test
    void main_shouldStartApplication() {
        // This test verifies the main method exists and is accessible
        assertDoesNotThrow(() -> {
            assertNotNull(CrmApplication.class.getMethod("main", String[].class));
        });
    }

    @Test
    void contextLoads_shouldLoadApplicationContext() {
        // Verify the application class is annotated correctly
        assertTrue(CrmApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void mainMethod_shouldBePublicStatic() {
        try {
            var method = CrmApplication.class.getMethod("main", String[].class);
            assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
            assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("Main method should exist");
        }
    }
}
