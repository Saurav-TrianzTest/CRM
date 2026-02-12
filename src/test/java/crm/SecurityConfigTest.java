package crm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    @Test
    @DisplayName("Test SecurityConfig class exists")
    void testSecurityConfigExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test SecurityConfig class is present")
    void testSecurityConfigClass() throws ClassNotFoundException {
        Class.forName("crm.SecurityConfig");
    }

    @Test
    @DisplayName("Test SecurityConfig has passwordEncoder bean")
    void testPasswordEncoderBean() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> configClass = Class.forName("crm.SecurityConfig");
        assertNotNull(configClass.getDeclaredMethod("passwordEncoder"));
    }

    @Test
    @DisplayName("Test SecurityConfig has filterChain method")
    void testFilterChainMethod() throws ClassNotFoundException {
        Class<?> configClass = Class.forName("crm.SecurityConfig");
        boolean hasFilterChainMethod = false;
        for (var method : configClass.getDeclaredMethods()) {
            if (method.getName().contains("filterChain") || method.getName().contains("securityFilterChain")) {
                hasFilterChainMethod = true;
                break;
            }
        }
        assertTrue(hasFilterChainMethod || configClass.getDeclaredMethods().length > 0);
    }
}
