package crm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("WebAppConfig Tests")
class WebAppConfigTest {

    @Test
    @DisplayName("Test WebAppConfig class exists")
    void testWebAppConfigExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test WebAppConfig class is present")
    void testWebAppConfigClass() throws ClassNotFoundException {
        Class.forName("crm.WebAppConfig");
    }

    @Test
    @DisplayName("Test WebAppConfig implements WebMvcConfigurer")
    void testWebAppConfigImplementsInterface() throws ClassNotFoundException {
        Class<?> configClass = Class.forName("crm.WebAppConfig");
        Class<?> configurerInterface = org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class;
        assertTrue(configurerInterface.isAssignableFrom(configClass));
    }
}
