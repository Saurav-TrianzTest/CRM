package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class CrmApplicationTest {

    @Test
    void testMainMethodExists() {
        try {
            CrmApplication.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("main method should exist");
        }
    }

    @Test
    void testCrmApplicationInstantiation() {
        CrmApplication app = new CrmApplication();
        assertNotNull(app);
    }

    @Test
    void testCrmApplicationClassExists() {
        assertNotNull(CrmApplication.class);
    }

    @Test
    void testSpringBootApplicationAnnotation() {
        assertTrue(CrmApplication.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}
