package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

public class CrmApplicationTest {

    @Test
    public void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            CrmApplication.main(new String[]{});
        });
    }

    @Test
    public void testApplicationContextLoads() {
        assertDoesNotThrow(() -> {
            String[] args = {"--spring.main.web-environment=false"};
            CrmApplication.main(args);
        });
    }

    @Test
    public void testCrmApplicationInstantiation() {
        CrmApplication app = new CrmApplication();
        assertNotNull(app);
    }
}
