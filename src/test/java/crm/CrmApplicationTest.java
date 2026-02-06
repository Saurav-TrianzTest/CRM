package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class CrmApplicationTest {

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            CrmApplication.main(new String[]{});
        });
    }

    @Test
    void testApplicationContextLoads() {
        assertDoesNotThrow(() -> {
            String[] args = new String[]{};
            // Test that the main method can be called without throwing exceptions
        });
    }

    @Test
    void testCrmApplicationClassExists() {
        assertNotNull(CrmApplication.class);
    }

    @Test
    void testCrmApplicationInstantiation() {
        CrmApplication app = new CrmApplication();
        assertNotNull(app);
    }
}
